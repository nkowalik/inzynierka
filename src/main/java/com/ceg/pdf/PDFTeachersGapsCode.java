package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;

import java.io.IOException;
import java.util.List;

/**
 * 
 * @author Martyna
 */
public class PDFTeachersGapsCode extends PDFGapsCode {
    PDFTeachersGapsCode(List<String> lines) throws IOException, EmptyPartOfTaskException {
        super(lines);
        answer = new PDFGapsTeachersAnswer(lines, textWidth, pdfLine.getFont(), pdfLine.getFontSize(), leftMargin);
    }
}
