/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.pdf;

import com.ceg.examContent.Exam;
import com.ceg.exceptions.EmptyPartOfTaskException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Martyna
 */
public class PDFTeachersAnswer extends PDFAnswer {
    //pole przechowujace listę odpowiedzi do zadania
    private List<String> answers;
    
    //pole przechowujące informację, który indeks z listy odpowiedzi będzie wykorzystany w przypadku wystąpienia 
    //następnej luki
    private int answersIndex;

    public PDFTeachersAnswer(int textWidth, String fontName, int fontSize) throws IOException {
        super(textWidth, fontName, fontSize);
        answersIndex = 0;
    }       
    
    /* funkcja zapisująca do dokumentu pdf najpierw odpowiedzi dla nauczyciela a następnie całą treść zadania */
    @Override
    public int writeToPDF(int x, int y) throws IOException, EmptyPartOfTaskException { 
        if (answers.isEmpty())
            throw new EmptyPartOfTaskException();
        answersIndex = 0;
        
        if (answers != null) {
            float answerWidth;
            PDFSettings pdfSettings = Exam.getInstance().pdfSettings;
            PDFLine line = new PDFLine(pdfSettings.getCommandFont(), pdfSettings.getCommandFontSize());
            int myY = y;
            for (String i : actualTaskLines) {
                String[] answersPlaces;
                answersPlaces = i.split("#placeForAnswer");
                
                for (String j: answersPlaces) {
                    answerWidth = getWidth(j);
                    line.setText(answers.get(answersIndex++));
                    line.writeLine(x + (int)answerWidth + 2, myY);
                }
                myY -= lineHeight;
            }            
        }
        return super.writeToPDF(x, y);
    }
    
    @Override
    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
