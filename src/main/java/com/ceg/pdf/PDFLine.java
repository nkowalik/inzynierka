package com.ceg.pdf;

import static com.ceg.pdf.PDFGenerator.cs;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

/**
 *
 * @author Martyna
 */
public class PDFLine {
    private String lineText;
    private PDType0Font font;
    private final int fontSize;
    
    public PDFLine(PDType0Font font, int fontSize) {
        this.font = font;
        this.fontSize = fontSize;
    }
    
    public PDFLine(String fontName, int fontSize) throws IOException {
        this.fontSize = fontSize;
        chooseFont(fontName);
    }
    
    public void setText(String text) {
        lineText = text;
    }
    public PDType0Font getFont() {
        return font;
    }
    public void setFont(PDType0Font font) {
        this.font = font;
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
    
    private void chooseFont(String fontName) throws IOException {
        File fontFile;
        
        switch(fontName) {
            case "Times New Roman":
                fontFile = new File("fonts/times.ttf");
                break;
            case "Arial":
                fontFile = new File("fonts/arial.ttf");
                break;
            case "Arial Narrow":
                fontFile = new File("fonts/ARIALN.ttf");
                break;
            case "Courier":
                fontFile = new File("fonts/cour.ttf");
                break;
            case "Verdana":
                fontFile = new File("fonts/verdana.ttf");
                break;
            default:
                fontFile = new File("fonts/times.ttf");
                break;
        }
        
        font = PDType0Font.load(PDFGenerator.document, fontFile);
       
    }
}
