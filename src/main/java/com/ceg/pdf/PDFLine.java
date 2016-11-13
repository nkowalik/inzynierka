package com.ceg.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PDFLine {
    private List<PDFLinePart> lineParts;
    private final int fontSize;
    private final int leftMargin;
    
    public PDFLine(int fontSize, int leftMargin) throws IOException {
        this.fontSize = fontSize;
        this.lineParts = new ArrayList<>();
        this.leftMargin = leftMargin;
    }
    
    public void writeLine(int startY) throws IOException { 
        float x = leftMargin;
        for (PDFLinePart lp : lineParts) {
            x = lp.writeLinePart(x, startY, fontSize);
        }
    }
    
    public int getFontSize() {
        return fontSize;
    }
    
    public List<PDFLinePart> getLineParts() {
        return lineParts;
    }
    
    public void setLineParts(List<PDFLinePart> lineParts) {
        this.lineParts = lineParts;
    }
}
