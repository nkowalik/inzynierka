package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.utils.Alerts;
import com.ceg.utils.FontType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Martyna
 */

/* klasa odpowiedzialna za wyglad pola odpowiedzi w arkuszu pdf */
public class PDFAnswer extends PDFAbstractTaskPart {
    protected List<String> placesForAnswers = new ArrayList<>();
    private final String placeForAnswer = "_________";

    public PDFAnswer(List<String> lines, float pdfContentWidthPercentage) throws IOException {
        super();
        PDFSettings pdfSettings = PDFSettings.getInstance();
        textWidth = (int)Math.floor(pdfContentWidthPercentage * PDFSettings.pdfContentWidth);;
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
        boolean alert = false;
        pdfLines.clear();
        
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
                if (!alert) {
                    Alerts.tooNarrowPlaceForAnswer();
                    alert = true;
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
        for (int i = 0; i < pdfLines.size(); i++) {
            if (pdfLines.get(i).getLineParts().get(0).getText().contains("#placeForAnswer")) {
                pdfLines.get(i).getLineParts().get(0).setText(pdfLines.get(i).getLineParts().get(0).getText() + ' ');
                String[] list = pdfLines.get(i).getLineParts().get(0).getText().split("#placeForAnswer");
                String line = "";
                
                for (int j = 0; j < list.length - 1; j++) {
                    line += list[j] + placesForAnswers.get(answerIndex++);
                }
            pdfLines.get(i).getLineParts().get(0).setText(line + list[list.length - 1]);
            }
        }
        return super.writeToPDF(y);
    }
}
