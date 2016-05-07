package com.ceg.pdf;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PDFCode extends PDFAbstractTask {
    private String line;
    private final List<String> operatorList = Arrays.asList("//", 
                                                            ";", 
                                                            ",", 
                                                            "{", 
                                                            "(", 
                                                            "}", 
                                                            ")", 
                                                            "<<",
                                                            ">>",
                                                            "==",
                                                            "=",
                                                            ">",
                                                            "<",
                                                            " " );
    
    PDFCode(int startX, int startY, int textWidth) {
        super();
        font = PDType1Font.COURIER;
        this.startX = startX;
        this.finalY = startY;
        this.textWidth = textWidth;
    }
    
    /*  Funkcja formatująca kod do pdfa. Jako parametr przyjmuje listę linii kodu.
        Reaguje na spacje i tabulatory (jeśli używa się albo jednego albo drugiego.
        Nie przesyłać z enterami.    */
    public void textSplitting(List<String> codeLines) throws IOException {
        float actualLineWidth;
        for (String codeLine : codeLines) { 
            line = codeLine;
            line = ifTabDoTab(line);
            actualLineWidth = getWidth(line); 
            
            if (actualLineWidth <= textWidth) {
                writeLine(line);
            }
            
            else {
                for (String string : operatorList) {
                    if (splittingLine(string))
                        break;
                }
            }
        }
    }
    
    /*  Zamienia każdy tabulator na odpowiadającą mu ilość spacji   */
    private String ifTabDoTab(String line) {
        return line.replace("\t", "        ");
    }
    
    private boolean splittingLine(String operator) throws IOException {
        if (!line.contains(operator))
            return false;
        else {
            int lineLength = line.length();
            
            while (lineLength != 0) {
                int i = line.lastIndexOf(operator, lineLength);
                if (i < 0)
                    return false;
                String end;
                end = line.substring(0, i+1);
                float a = getWidth(end);
                float b = textWidth;
                if (getWidth(end) <= textWidth) {
                    writeLine(end);
                    line = line.substring(i+1);
                    i = line.length();
                    if (getWidth(line) <= textWidth) {
                        writeLine(line);
                        return true;
                    }
                }
                lineLength = i - 1;
            }
        }
        return true;
    }
}
