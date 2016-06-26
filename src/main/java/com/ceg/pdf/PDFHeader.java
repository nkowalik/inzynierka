package com.ceg.pdf;

import java.io.IOException;

/**
 *
 * @author Martyna
 */
public class PDFHeader {
    private final PDFLine line;
    
    public PDFHeader() throws IOException {
        this.line = new PDFLine("times", 12);
    }
    
        /* Funkcja dodająca nagłówek postaci miejsc na imię  i nazwisko oraz numer indeksu studenta */
    public int setHeader(int leftMargin, int topMargin, int breakAfterHeader, String testDate) throws IOException {        
        line.setText("________________________________________________________");
        line.writeLine(leftMargin, topMargin);
        
        line.setText("___________");
        line.writeLine(400, topMargin);
        
        line.setText(testDate);
        line.writeLine(501, topMargin);
        
        //przejście do następnej linii
        topMargin -= 15;
        line.setText("Imię i nazwisko");
        line.writeLine(leftMargin + 1, topMargin);
        
        line.setText("Nr indeksu");
        line.writeLine(401, topMargin);
        
        topMargin -= breakAfterHeader;
        
        return topMargin;
    }
}
