package com.ceg.pdf;

import com.ceg.utils.FontType;
import static com.ceg.utils.FontType.SERIF_REGULAR;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Martyna
 */
public class PDFHeader {
    private final FontType fontType = SERIF_REGULAR;
    private static int topMargin = PDFSettings.getInstance().topMargin;
    private static final int leftMargin = PDFSettings.getInstance().leftMargin;
    private static final String testDate = PDFSettings.getInstance().getDate();
    
        /* Funkcja dodająca nagłówek postaci miejsc na imię  i nazwisko oraz numer indeksu studenta */
    public int setHeader(int breakAfterHeader) throws IOException {
        PDFLine line = new PDFLine(12, leftMargin);
        PDFLinePart linePart = new PDFLinePart(fontType);
        linePart.setText("________________________________________________________");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(topMargin);
       
        line = new PDFLine(12, 400);
        linePart = new PDFLinePart(fontType);
        linePart.setText("___________");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(topMargin);
        
        line = new PDFLine(12, 501);
        linePart = new PDFLinePart(fontType);
        linePart.setText(testDate);
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(topMargin);
        
        //przejście do następnej linii
        topMargin -= 15;
        
        line = new PDFLine(12, leftMargin + 1);
        linePart = new PDFLinePart(fontType);
        linePart.setText("Imię i nazwisko");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(topMargin);
        
        line = new PDFLine(12, 401);
        linePart = new PDFLinePart(fontType);
        linePart.setText("Nr indeksu");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(topMargin);
        
        topMargin -= breakAfterHeader;
        
        return topMargin;
    }
}
