package com.ceg.pdf;

import com.ceg.utils.FontType;
import java.io.IOException;
import java.util.List;

public class PDFGapsTeachersAnswer extends PDFTeachersAnswer {

    public PDFGapsTeachersAnswer(List<String> lines, int textWidth, FontType font, int fontSize, int leftMargin) throws IOException {
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
        
        super.setAnswers(answers);
    }   
}
