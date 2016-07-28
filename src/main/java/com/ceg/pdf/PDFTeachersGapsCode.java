package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Martyna.Luczkowska on 2016-07-28.
 */
public class PDFTeachersGapsCode extends PDFGapsCode {
    public PDFTeachersAnswer answer;

    PDFTeachersGapsCode(List<String> lines) throws IOException, EmptyPartOfTaskException {
        super(lines);
        answer = new PDFTeachersAnswer(textWidth, pdfLine.getFont(), pdfLine.getFontSize(), leftMargin);
    }
}
