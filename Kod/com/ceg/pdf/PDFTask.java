/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.pdf;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 *
 * @author Martyna
 */
public abstract class PDFTask {
    protected static PDFont font;
    protected static int fontSize = 12;
    protected static PDPageContentStream cs = PDFGenerator.cs;
    protected final static int lineHeight = 15;
    protected int finalY;
    protected int startX;
    protected int textWidth;
    protected float actualWidth = 0;    
    
    public int getFinalY() {
        return finalY;
    }    
        
    protected float getWidth(String text) throws IOException {
        return font.getStringWidth(text) / 1000 * fontSize;
    }
    
    public void writeLine(String text, int startX, int startY) throws IOException {
        cs.setFont(font, fontSize);
        cs.beginText();
        cs.newLineAtOffset(startX, startY);
        cs.showText(text);
        cs.endText();
        
        finalY -= lineHeight;
    }
}
