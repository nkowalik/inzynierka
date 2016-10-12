package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PDFCode extends PDFAbstractTaskPart {
    private String line;
    public PDFAnswer answer = null;

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
    
    PDFCode(List<String> lines) throws IOException, EmptyPartOfTaskException {
        super();
        PDFSettings pdfSettings = PDFSettings.getInstance();
        textWidth = pdfSettings.codeWidth;
        leftMargin = pdfSettings.leftCodeMargin;
        defaultFontType = pdfSettings.getCodeFont();
        fontSize = pdfSettings.getCodeFontSize();
        textSplitting(lines);
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
            actualLineWidth = getWidth(line, defaultFontType, fontSize); 
            
            if (actualLineWidth <= textWidth) {
                PDFLine pdfLine = new PDFLine(fontSize, leftMargin);
                PDFLinePart lp = new PDFLinePart(defaultFontType);
                lp.setText(line);
                pdfLine.setLineParts(Arrays.asList(lp));
                pdfLines.add(pdfLine);
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
                if (getWidth(end, defaultFontType, fontSize) <= textWidth) {
                    PDFLine pdfLine = new PDFLine(fontSize, leftMargin);
                    PDFLinePart lp = new PDFLinePart(defaultFontType);
                    lp.setText(end);
                    pdfLine.setLineParts(Arrays.asList(lp));
                    pdfLines.add(pdfLine);
                    line = line.substring(i+1);
                    i = line.length();
                    if (getWidth(line, defaultFontType, fontSize) <= textWidth) {
                        pdfLine = new PDFLine(fontSize, leftMargin);
                        lp = new PDFLinePart(defaultFontType);
                        lp.setText(line);
                        pdfLine.setLineParts(Arrays.asList(lp));
                        pdfLines.add(pdfLine);
                        return true;
                    }
                }
                lineLength = i - 1;
            }
        }
        return true;
    }

    protected void setAnswer(PDFAnswer answer) {
    }
}
