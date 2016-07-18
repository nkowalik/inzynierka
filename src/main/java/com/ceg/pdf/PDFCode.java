package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
    
    PDFCode(int textWidth, String fontName, int fontSize) throws IOException {
        super(textWidth, fontName, fontSize);
    }
    
    /*  Funkcja formatująca kod do pdfa. Jako parametr przyjmuje listę linii kodu.
        Reaguje na spacje i tabulatory (jeśli używa się albo jednego albo drugiego.
        Nie przesyłać z enterami.    */   
    @Override
    public void textSplitting (List<String> codeLines) throws IOException, EmptyPartOfTaskException {
        super.textSplitting(codeLines);
        float actualLineWidth;
        for (String codeLine : codeLines) { 
            line = codeLine;
            line = ifTabDoTab(line);
            actualLineWidth = getWidth(line); 
            
            if (actualLineWidth <= textWidth) {
                actualTaskLines.add(line);
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
                if (getWidth(end) <= textWidth) {
                    actualTaskLines.add(end);
                    line = line.substring(i+1);
                    i = line.length();
                    if (getWidth(line) <= textWidth) {
                        actualTaskLines.add(line);
                        return true;
                    }
                }
                lineLength = i - 1;
            }
        }
        return true;
    }
}
