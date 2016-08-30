package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Martyna.Luczkowska on 2016-07-28.
 */
public class PDFGapsCode extends PDFCode {

    PDFGapsCode(List<String> lines) throws IOException, EmptyPartOfTaskException {
        super(lines);
        answer = new PDFGapsAnswer(lines, textWidth, pdfLine.getFont(), pdfLine.getFontSize(), leftMargin);
    }

    @Override
    public int writeToPDF(int y) throws IOException, EmptyPartOfTaskException {
        answer.actualTaskLines = this.actualTaskLines;
        return answer.writeToPDF(y);
    }
}
