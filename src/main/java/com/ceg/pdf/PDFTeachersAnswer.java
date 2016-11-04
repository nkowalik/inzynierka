package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.utils.FontType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class PDFTeachersAnswer extends PDFAnswer {
    //pole przechowujace listę odpowiedzi do zadania
    protected List<String> answers;
    
    //pole przechowujące informację, który indeks z listy odpowiedzi będzie wykorzystany w przypadku wystąpienia 
    //następnej luki
    private int answersIndex;

    public PDFTeachersAnswer(List<String> lines) throws IOException {
        super(lines);
        answersIndex = 0;
    }

    public PDFTeachersAnswer(List<String> lines, int textWidth, FontType font, int fontSize, int leftMargin) throws IOException {
        super(lines, textWidth, font, fontSize, leftMargin);
        answersIndex = 0;
    }
    
    /* funkcja zapisująca do dokumentu pdf najpierw odpowiedzi dla nauczyciela a następnie całą treść zadania */
    @Override
    public int writeToPDF(int y) throws IOException, EmptyPartOfTaskException {
        if (answers.isEmpty())
            throw new EmptyPartOfTaskException();
        answersIndex = 0;
        
        if (answers != null) {
            float answerWidth;
            int myY = y;
            for (PDFLine i : pdfLines) {
                String[] answersPlaces;
                if (i.getLineParts().get(0).getText().contains("#placeForAnswer")) {
                    i.getLineParts().get(0).setText(i.getLineParts().get(0).getText() + ' ');
                    answersPlaces = i.getLineParts().get(0).getText().split("#placeForAnswer");               

                    for (int j = 0; j < answersPlaces.length - 1; j++) {
                        answerWidth = getWidth(answersPlaces[j], defaultFontType, fontSize);
                        PDFLine pdfLine = new PDFLine(fontSize, leftMargin + (int)answerWidth + 2);
                        PDFLinePart linePart = new PDFLinePart(defaultFontType);
                        linePart.setText(answers.get(answersIndex++));
                        pdfLine.setLineParts(Arrays.asList(linePart));
                        pdfLine.writeLine(myY);
                    }
                }
                myY -= lineHeight;
            }            
        }
        return super.writeToPDF(y);
    }
    
    @Override
    public void setAnswers(List<String> answers) {
        this.answers = answers;
        super.setAnswers(answers);
    }
}
