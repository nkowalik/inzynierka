/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.pdf;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author Martyna
 */
public class PDFCode extends PDFTask {
    
    PDFCode(int startX, int startY, int textWidth) {
        font = PDType1Font.COURIER;
        this.startX = startX;
        this.finalY = startY;
        this.textWidth = textWidth;
    }
    
    public void textSplitting(List<String> codeLines) throws IOException {
        float actualLineWidth;
        for (String codeLine : codeLines) {
            actualLineWidth = getWidth(codeLine); 
            
            if (actualLineWidth <= textWidth) {
                writeLine(codeLine, startX, finalY);
            }
            
            else {
                
            }
        }
    }
}
