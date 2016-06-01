package com.ceg.pdf;

import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFCommand extends PDFAbstractTask {
    private String[] words;
    private String line = "";
    private final float spaceWidth;
        
    PDFCommand(int textWidth, PDType1Font font, int fontSize) throws IOException {
        super(textWidth, font, fontSize);
        spaceWidth = getWidth(" ");
    }
    
    /*  Funkcja odpowiedzialna za formatowanie tekstu polecenia. Argumentem jest tekst polecenia.
        Dzieli wyrazy po spacji i układa jak najwięcej w jednej linii. Wrażliwa na znaki entera:
        każdy enter w poleceniu będzie widoczny w dokumencie pdf. Brak możliwości używania innych
        białych znaków poza spacją i enterem. */    
    @Override
    public void textSplitting (List<String> command) throws IOException {
        String string = mergeStringList(command);
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
                actualTaskLines.add(line);
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
            actualTaskLines.add(line);
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
                actualTaskLines.add(line);
                line = "";
            }
            
            if (!line.isEmpty()) {
                if (!lineIsEmpty) {
                    line += ' ';
                    lineIsEmpty = true;
                }
                else {
                    actualTaskLines.add(line);
                    line = "";
                    actualWidth = 0;
                }
            }
            
            line += word2;   
            actualWidth += actualWordWidth;
        }
    }
    
    private String mergeStringList(List<String> command) {
        String txt = new String();
        // pobranie treści polecenia w formie listy, sklejenie jej w jednego Stringa      
        for (int index = 0 ; index < command.size(); index++) {
            String line = command.get(index);
            txt+=line+"\n";
        }
        return txt;
    }
}
