package com.ceg.pdf;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFCommand extends PDFAbstractTask {
    private String[] words;
    private String line = "";
    private final float spaceWidth;
        
    PDFCommand(int startX, int startY, int textWidth) throws IOException {
        super();
        font = PDType1Font.TIMES_ROMAN;
        spaceWidth = getWidth(" ");
        this.startX = startX;
        this.textWidth = textWidth;
        this.finalY = startY;
    }
    
    /*  Funkcja odpowiedzialna za formatowanie tekstu polecenia. Argumentem jest tekst polecenia.
        Dzieli wyrazy po spacji i układa jak najwięcej w jednej linii. Wrażliwa na znaki entera:
        każdy enter w poleceniu będzie widoczny w dokumencie pdf. Brak możliwości używania innych
        białych znaków poza spacją i enterem. */
    public void textSplitting (String string) throws IOException {
        float actualWordWidth;
        words = string.split(" ");        
        
        for (String word : words) {
            //jeśli występują w wyrazie entery to obsługuje je
            if (word.contains("\n")) {
                doEnter(word);
                continue;
            }
            actualWordWidth = getWidth(word);
            
            //linia wystarczająco długa, żaden wyraz więcej się nie zmieści
            if (actualWidth + actualWordWidth + spaceWidth  >= textWidth) {
                writeLine(line);
                line = word;
                actualWidth = actualWordWidth;
            }
            //do linii dopisujemy wyraz
            else {
                //jeśli linia nie jest pusta to dodajemy spację po poprzednim wyrazie
                if (!line.isEmpty()) {
                    line += ' ';
                    actualWidth += spaceWidth;
                }
                line += word;
                actualWidth += actualWordWidth;
            }
        }
        //jeśli linia nie jest pusta, wypisujemy ją
        if (!line.isEmpty()) {
            writeLine(line);
        }
    }
    
    /*  Funkcja służąca do zrobienia obowiązkowego entera */
    private void doEnter(String word) throws IOException {
        String[] words2 = word.split("\n");
        float actualWordWidth;
        
        //zmienna służąca do określenia czy jesteśmy w pierwszej linii, żeby nie wykonywać nadmiernego entera
        boolean lineIsEmpty = false;
        
        if (line.isEmpty()) 
            lineIsEmpty = true;
        
        for (String word2 : words2) {
            actualWordWidth = getWidth(word2);
            
            //linia wystarczająco długa, zapisujemy
            if (actualWidth + actualWordWidth + spaceWidth  >= textWidth) {
                writeLine(line);
                line = "";
            }
            
            if (!line.isEmpty()) {
                if (!lineIsEmpty) {
                    line += ' ';
                    lineIsEmpty = true;
                }
                else {
                    writeLine(line);
                    line = "";
                    actualWidth = 0;
                }
            }
            
            line += word2;   
            actualWidth += actualWordWidth;
        }
    }
}
