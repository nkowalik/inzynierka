package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

/**
 *
 * @author Martyna
 */
public class PDFGapsAnswer extends PDFAnswer { 

    public PDFGapsAnswer(List<String> lines, int textWidth, PDType0Font font, int fontSize, int leftMargin) throws IOException {
        super(lines, textWidth, font, fontSize, leftMargin);
    }
    
    @Override
    public void setAnswers(List<String> answers) {         
        answers.stream().map((s) -> s.length() + 1).map((placeForAnswerLength) -> {
            String placeForAnswer = "";
            for (int i = 0; i < placeForAnswerLength; i++) {
                placeForAnswer += '_';
            }
            return placeForAnswer;
        }).forEach((placeForAnswer) -> {
            placesForAnswers.add(placeForAnswer);
        });
    }   
}
