package com.ceg.pdf;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

public abstract class PDFAbstractTask {
    protected static PDFont font;
    protected static int fontSize = 10;
    protected static PDPageContentStream cs = PDFGenerator.cs;
    protected final static int lineHeight = 15;
    protected int finalY;
    protected int startX;
    protected int textWidth;
    protected float actualWidth = 0;    
    
    public int getFinalY() {
        return finalY;
    }    
       
    /*  Wyrzuca szerokość tekstu napisanego daną czcionką o konkretnym rozmiarze    */
    protected float getWidth(String text) throws IOException {
        return font.getStringWidth(text) / 1000 * fontSize;
    }
    
    /*  Zapisuje linię do dokumentu i przechodzi do następnej   */
    public void writeLine(String text) throws IOException {
        cs.setFont(font, fontSize);
        cs.beginText();
        cs.newLineAtOffset(startX, finalY);
        cs.showText(text);
        cs.endText();
        
        finalY -= lineHeight;
    }
}
