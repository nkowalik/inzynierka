package com.ceg.pdf;

import static com.ceg.pdf.PDFGenerator.cs;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 *
 * @author Martyna
 */
public class PDFLine {
    private String lineText;
    private final PDFont font;
    private final int fontSize;
    
    public PDFLine(PDFont font, int fontSize) {
        this.font = font;
        this.fontSize = fontSize;
    }
    
    public void setText(String text) {
        lineText = text;
    }
    public PDFont getFont() {
        return font;
    }
    public int getFontSize() {
        return fontSize;
    }
    
    public void writeLine(int startX, int startY) throws IOException {      
        cs.setFont(font, fontSize);
        
        cs.beginText();
        cs.newLineAtOffset(startX, startY);
        cs.showText(lineText);
        cs.endText();
    }
}
