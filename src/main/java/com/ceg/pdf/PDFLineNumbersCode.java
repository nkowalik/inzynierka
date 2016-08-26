package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Martyna
 */
public class PDFLineNumbersCode extends PDFCode {
    private final float linePosition = leftMargin + getWidth("   ");
    private final float lineWidth = (float) 0.1;
    
    public PDFLineNumbersCode(List<String> lines) throws IOException, EmptyPartOfTaskException {
        super(lines);
    }
    
    @Override
    public int writeToPDF(int y) throws IOException, EmptyPartOfTaskException {
        for (int i = 0; i < actualTaskLines.size(); i++) {
            actualTaskLines.set(i, i+1 + ":  " + actualTaskLines.get(i));
        }
        int tempY = super.writeToPDF(y);
        
        PDFGenerator.cs.moveTo(linePosition, tempY);
        PDFGenerator.cs.lineTo(linePosition, y + lineHeight);
        PDFGenerator.cs.setLineWidth(lineWidth);
        PDFGenerator.cs.stroke();
        return tempY;
    }
    
}
