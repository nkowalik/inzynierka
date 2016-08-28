package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author Martyna
 */
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

    public PDFTeachersAnswer(List<String> lines, int textWidth, PDType0Font font, int fontSize, int leftMargin) throws IOException {
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
            for (String i : actualTaskLines) {
                String[] answersPlaces;
                if (i.contains("#placeForAnswer")) {
                    i += ' ';
                    answersPlaces = i.split("#placeForAnswer");               

                    for (int j = 0; j < answersPlaces.length - 1; j++) {
                        answerWidth = getWidth(answersPlaces[j]);
                        pdfLine.setText(answers.get(answersIndex++));
                        pdfLine.writeLine(leftMargin + (int)answerWidth + 2, myY);
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
