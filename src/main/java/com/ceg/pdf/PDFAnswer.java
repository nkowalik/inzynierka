package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martyna
 */

/* klasa odpowiedzialna za wyglad pola odpowiedzi w arkuszu pdf */
public class PDFAnswer extends PDFAbstractTaskPart {
    protected List<String> placesForAnswers = new ArrayList<>();
    private final String placeForAnswer = "_________";

    public PDFAnswer(List<String> lines) throws IOException {
        super();
        PDFSettings pdfSettings = PDFSettings.getInstance();
        textWidth = PDFSettings.commandWidth;
        pdfLine = new PDFLine(pdfSettings.getCommandFont(), pdfSettings.getCommandFontSize());
        leftMargin = pdfSettings.leftMargin;
        lineHeight+=2;
        textSplitting(lines);
    }

    public PDFAnswer(List<String> lines, int textWidth, PDType0Font font, int fontSize, int leftMargin) throws IOException {
        super();
        this.textWidth = textWidth;
        pdfLine = new PDFLine(font, fontSize);
        this.leftMargin = leftMargin;
        lineHeight+=2;
    }
    
    @Override
    public void textSplitting (List<String> lines) throws IOException {
        float actualLineWidth;
        
        for (String line : lines) {
            actualLineWidth = getWidth(line);
            
            if (actualLineWidth <= textWidth) {
                actualTaskLines.add(line);
            }
            else {
                String[] words = line.split(" ");        
                float actualWordWidth;
                float spaceWidth = getWidth(" ");
                
                for (String word : words) {
                    actualWordWidth = getWidth(word);
            
                    //linia wystarczająco długa, żaden wyraz więcej się nie zmieści
                    if (actualWidth + actualWordWidth + spaceWidth  >= textWidth) {
                        actualTaskLines.add(line);
                        line = word;
                        actualWidth = actualWordWidth;
                    }
                    //do linii dopisujemy wyraz
                    else {
                        //jeśli linia nie jest pusta to dodajemy spację po poprzednim wyrazie
                        if (!line.isEmpty()) {
                            line += ' ';
                            actualWidth += spaceWidth;
                        }
                        line += word;
                        actualWidth += actualWordWidth;
                    }
                }
                //jeśli linia nie jest pusta, wypisujemy ją
                if (!line.isEmpty()) {
                    actualTaskLines.add(line);
                }
            }
        }
    }
    
    public void setAnswers(List<String> answers) {   
        answers.stream().forEach((_item) -> {
            placesForAnswers.add(placeForAnswer);
        });
    }
    
    @Override
    public int writeToPDF(int y) throws IOException, EmptyPartOfTaskException {
        int answerIndex = 0;
        for (int i=0; i<actualTaskLines.size(); i++) {
            if (actualTaskLines.get(i).contains("#placeForAnswer")) {
                actualTaskLines.set(i, actualTaskLines.get(i) + ' ');
                String[] list = actualTaskLines.get(i).split("#placeForAnswer");
                String line = "";
                
                for (int j = 0; j < list.length - 1; j++) {
                    line += list[j] + placesForAnswers.get(answerIndex++);
                }
            actualTaskLines.set(i, line + list[list.length - 1]);
            }
        }
        return super.writeToPDF(y);
    }
}
