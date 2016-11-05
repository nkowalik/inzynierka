package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;

import java.io.IOException;
import java.util.List;


public class PDFGapsCode extends PDFCode {

    PDFGapsCode(List<String> lines, float pdfContentWidthPercentage) throws IOException, EmptyPartOfTaskException {
        super(lines, pdfContentWidthPercentage);
        answer = new PDFGapsAnswer(lines, textWidth, defaultFontType, fontSize, leftMargin);
    }

    @Override
    public int writeToPDF(int y) throws IOException, EmptyPartOfTaskException {
        answer.pdfLines = this.pdfLines;
        return answer.writeToPDF(y);
    }
}
