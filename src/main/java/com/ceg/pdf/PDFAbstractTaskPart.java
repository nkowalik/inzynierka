package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class PDFAbstractTaskPart {
    protected PDFLine pdfLine;
    protected int lineHeight;
    protected static PDPageContentStream cs;
    protected int textWidth;
    protected float actualWidth = 0;  
    protected ArrayList<String> actualTaskLines;
    protected int leftMargin = 0;
    
    PDFAbstractTaskPart() throws IOException{
        PDFSettings pdfSettings = PDFSettings.getInstance();
        cs = PDFGenerator.cs;
        actualTaskLines = new ArrayList<>();
        pdfLine = new PDFLine(pdfSettings.getCodeFont(), pdfSettings.getCodeFontSize());
        this.textWidth = 0;
        lineHeight = pdfSettings.getCodeFontSize();
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
    
    public void textSplitting (List<String> command) throws IOException, EmptyPartOfTaskException {
        if (command.isEmpty())
            throw new EmptyPartOfTaskException();
    }
    
    public int writeToPDF(int y) throws IOException, EmptyPartOfTaskException {
        if (actualTaskLines.isEmpty())
            throw new EmptyPartOfTaskException();
        
        for (String i : actualTaskLines) {
            pdfLine.setText(i);
            pdfLine.writeLine(leftMargin, y);
            y -= lineHeight;
        }
        actualTaskLines.clear();
        return y;
    }
}
