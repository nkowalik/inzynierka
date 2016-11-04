package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.utils.FontType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/* klasa odpowiedzialna za wyglad pola odpowiedzi w arkuszu pdf */
public class PDFAnswer extends PDFAbstractTaskPart {
    protected List<String> placesForAnswers = new ArrayList<>();
    private final String placeForAnswer = "_________";

    public PDFAnswer(List<String> lines) throws IOException {
        super();
        PDFSettings pdfSettings = PDFSettings.getInstance();
        textWidth = PDFSettings.commandWidth;
        leftMargin = pdfSettings.leftMargin;
        lineHeight+=2;
        defaultFontType = pdfSettings.getCommandFont();
        fontSize = pdfSettings.getCommandFontSize();
        textSplitting(lines);
    }

    public PDFAnswer(List<String> lines, int textWidth, FontType font, int fontSize, int leftMargin) throws IOException {
        super();
        this.textWidth = textWidth;
        this.defaultFontType = font;
        this.leftMargin = leftMargin;
        this.fontSize = fontSize;
        lineHeight+=2;
    }
    
    @Override
    public void textSplitting (List<String> lines) throws IOException {
        float actualLineWidth;
        
        for (String line : lines) {
            actualLineWidth = getWidth(line, defaultFontType, fontSize);
            
            if (actualLineWidth <= textWidth) {
                PDFLine pdfLine = new PDFLine(fontSize, leftMargin);
                PDFLinePart lp = new PDFLinePart(defaultFontType);
                lp.setText(line);
                pdfLine.setLineParts(Arrays.asList(lp));
                pdfLines.add(pdfLine);
            }
            else {
                String[] words = line.split(" ");        
                float actualWordWidth;
                float spaceWidth = getWidth(" ", defaultFontType, fontSize);
                
                for (String word : words) {
                    actualWordWidth = getWidth(word, defaultFontType, fontSize);
            
                    //linia wystarczająco długa, żaden wyraz więcej się nie zmieści
                    if (actualWidth + actualWordWidth + spaceWidth  >= textWidth) {
                        PDFLine pdfLine = new PDFLine(fontSize, leftMargin);
                        PDFLinePart lp = new PDFLinePart(defaultFontType);
                        lp.setText(line);
                        pdfLine.setLineParts(Arrays.asList(lp));
                        pdfLines.add(pdfLine);
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
                    PDFLine pdfLine = new PDFLine(fontSize, leftMargin);
                    PDFLinePart lp = new PDFLinePart(defaultFontType);
                    lp.setText(line);
                    pdfLine.setLineParts(Arrays.asList(lp));
                    pdfLines.add(pdfLine);
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
        for (PDFLine pdfLine : pdfLines) {
            if (pdfLine.getLineParts().get(0).getText().contains("#placeForAnswer")) {
                pdfLine.getLineParts().get(0).setText(pdfLine.getLineParts().get(0).getText() + ' ');
                String[] list = pdfLine.getLineParts().get(0).getText().split("#placeForAnswer");
                String line = "";

                for (int j = 0; j < list.length - 1; j++) {
                    line += list[j] + placesForAnswers.get(answerIndex++);
                }
                pdfLine.getLineParts().get(0).setText(line + list[list.length - 1]);
            }
        }
        return super.writeToPDF(y);
    }
}
