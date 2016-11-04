package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Martyna
 */
public class PDFLineNumbersCode extends PDFCode {
    private final float linePosition = leftMargin + getWidth("   ", defaultFontType, fontSize);
    private final float lineWidth = (float) 0.1;
    
    public PDFLineNumbersCode(List<String> lines, float pdfContentWidthPercentage) throws IOException, EmptyPartOfTaskException {
        super(lines, pdfContentWidthPercentage);
    }
    
    @Override
    public int writeToPDF(int y) throws IOException, EmptyPartOfTaskException {
        for (int i = 0; i < pdfLines.size(); i++) {
            pdfLines.get(i).getLineParts().get(0).setText(i+1 + ":  " + pdfLines.get(i).getLineParts().get(0).getText());
        }
        int tempY = super.writeToPDF(y);
        
        PDFGenerator.cs.moveTo(linePosition, tempY);
        PDFGenerator.cs.lineTo(linePosition, y + lineHeight);
        PDFGenerator.cs.setLineWidth(lineWidth);
        PDFGenerator.cs.stroke();
        return tempY;
    }
    
}
