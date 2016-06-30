package com.ceg.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public abstract class PDFAbstractTask {
    protected PDFLine pdfLine;
    protected int lineHeight;
    protected static PDPageContentStream cs;
    protected int textWidth;
    protected float actualWidth = 0;  
    protected ArrayList<String> actualTaskLines;
    
    public PDFAbstractTask(int textWidth, String fontName, int fontSize) throws IOException{
        cs = PDFGenerator.cs;
        actualTaskLines = new ArrayList<>();
        pdfLine = new PDFLine(fontName, fontSize);
        this.textWidth = textWidth;
        lineHeight = fontSize;
    } 
    public int getNumberOfLines() {
        return actualTaskLines.size();
    }
    public int getLineHeight() {
       return lineHeight; 
    }
    
    /*  Wyrzuca szerokość tekstu napisanego daną czcionką o konkretnym rozmiarze    */
    protected float getWidth(String text) throws IOException {
        return pdfLine.getFont().getStringWidth(text) / 1000 * pdfLine.getFontSize();
    }
    
    public void textSplitting (List<String> command) throws IOException {
    }
    
    public int writeToPDF(int x, int y) throws IOException{ 
        for (String i : actualTaskLines) {
            pdfLine.setText(i);
            pdfLine.writeLine(x, y);
            y -= lineHeight;
        }
        actualTaskLines.clear();
        return y;
    }
}
