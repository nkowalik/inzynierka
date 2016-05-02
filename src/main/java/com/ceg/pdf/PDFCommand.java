/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.pdf;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author Martyna
 */
public class PDFCommand extends PDFTask {   
    private String[] words;
    private String line = "";
    private final float spaceWidth;
        
    PDFCommand(int startX, int startY, int textWidth) throws IOException {
        font = PDType1Font.TIMES_ROMAN;
        spaceWidth = getWidth(" ");
        this.startX = startX;
        this.textWidth = textWidth;
        this.finalY = startY;
    }
        
    public void textSplitting (String string) throws IOException {
        float actualWordWidth;
        words = string.split(" ");        
        
        for (String word : words) {
            if (word.contains("\n")) {
                doEnter(word);
                continue;
            }
            actualWordWidth = getWidth(word);
            if (actualWidth + actualWordWidth + spaceWidth  >= textWidth) {
                writeLine(line, startX, finalY);
                line = word;
                actualWidth = actualWordWidth;
            }
            else {
                if (!line.isEmpty()) {
                    line += ' ';
                    actualWidth += spaceWidth;
                }
                line += word;
                actualWidth += actualWordWidth;
            }
        }
        if (!line.isEmpty()) {
            writeLine(line, startX, finalY);
        }
    }
        
    private void doEnter(String word) throws IOException {
        String[] words2 = word.split("\n");
        float actualWordWidth;
        boolean lineIsEmpty = false;
        
        if (line.isEmpty()) 
            lineIsEmpty = true;
        
        for (String word2 : words2) {
            actualWordWidth = getWidth(word2);
            if (actualWidth + actualWordWidth + spaceWidth  >= textWidth) {
                writeLine(line, startX, finalY);
                line = "";
            }
            
            if (!line.isEmpty()) {
                if (!lineIsEmpty) {
                    line += ' ';
                    lineIsEmpty = true;
                }
                else {
                    writeLine(line, startX, finalY);
                    line = "";
                    actualWidth = 0;
                }
            }
            
            line += word2;   
            actualWidth += actualWordWidth;
        }
    }
}
