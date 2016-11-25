package com.ceg.pdf;

import com.ceg.utils.FontType;
import static com.ceg.utils.FontType.SERIF_REGULAR;
import java.io.IOException;
import java.util.Arrays;


public class PDFHeader {
    private final FontType fontType = SERIF_REGULAR;
    private final int fontSize = 12;
    private final int placeForIndexNumberX = 400;
    private final int placeForDateX = 501;
    private static int topMargin = PDFSettings.getInstance().topMargin;
    private static final int leftMargin = PDFSettings.getInstance().leftMargin;
    private static final String testDate = PDFSettings.getInstance().getDate();
    
    
        /* Funkcja dodająca nagłówek postaci miejsc na imię  i nazwisko oraz numer indeksu studenta */
    public int setHeader(int breakAfterHeader) throws IOException {
        int top = topMargin;
        PDFLine line = new PDFLine(fontSize, leftMargin);
        PDFLinePart linePart = new PDFLinePart(fontType);

        //przejście do następnej linii
        top -= 45;

        linePart.setText("________________________________________________________");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);
       
        line = new PDFLine(fontSize, placeForIndexNumberX);
        linePart = new PDFLinePart(fontType);
        linePart.setText("___________");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);
        
        line = new PDFLine(fontSize, placeForDateX);
        linePart = new PDFLinePart(fontType);
        linePart.setText(testDate);
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);

        top -= 15;
        
        line = new PDFLine(fontSize, leftMargin + 1);
        linePart = new PDFLinePart(fontType);
        linePart.setText("Imię i nazwisko");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);
        
        line = new PDFLine(fontSize, placeForIndexNumberX + 1);
        linePart = new PDFLinePart(fontType);
        linePart.setText("Nr indeksu");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);

        top -= 45;

        return top-breakAfterHeader;
    }
}
