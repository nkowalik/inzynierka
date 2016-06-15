package com.ceg.pdf;

import java.io.File;
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
    public int setHeader(int leftMargin, int topMargin, int breakAfterHeader) throws IOException {        
        line.setText("________________________________________________________");
        line.writeLine(leftMargin, topMargin);
        
        line.setText("___________");
        line.writeLine(500, topMargin);
        
        //przejście do następnej linii
        topMargin -= 15;
        line.setText("Imię i nazwisko");
        line.writeLine(leftMargin + 1, topMargin);
        
        line.setText("Nr indeksu");
        line.writeLine(501, topMargin);
        
        topMargin -= breakAfterHeader;
        
        return topMargin;
    }
}
