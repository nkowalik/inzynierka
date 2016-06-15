package com.ceg.pdf;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author Martyna
 */
public class PDFAnswer extends PDFAbstractTask {

    public PDFAnswer(int textWidth, String fontName, int fontSize) throws IOException {
        super(textWidth, fontName, fontSize);
    }
    
    @Override
    public void textSplitting (List<String> lines) throws IOException {
        float actualLineWidth;
        
        for (String line : lines) {
            actualLineWidth = getWidth(line);
            
            if (actualLineWidth <= textWidth) {
                actualTaskLines.add(line);
            }
            else {
                String[] words = line.split(" ");        
                float actualWordWidth;
                float spaceWidth = getWidth(" ");
                
                for (String word : words) {
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
        }
    }
}
