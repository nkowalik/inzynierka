package com.ceg.pdf;

import static com.ceg.pdf.PDFGenerator.cs;
import com.ceg.utils.FontType;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

/**
 *
 * @author Martyna
 */
public class PDFLinePart {
    private final FontType font;
    private String text;
    
    public PDFLinePart(FontType font) {
        this.font = font;
        text = "";
    }
    
    public float writeLinePart(float x, int y, int fontSize) throws IOException {
        if (!text.equals("")) {
            cs.beginText();
            cs.newLineAtOffset(x, y);
            cs.setFont(PDType0Font.load(PDFGenerator.document, new File(font.getFileName())), fontSize);
            cs.showText(text);
            cs.endText();
        }
        
        return x + getWidth(fontSize);
    }
    
    /**
     * Oblicza szerokość tekstu napisanego czcionką o konkretnym rozmiarze.
     * @param fontSize Rozmiar czcionki, dla której obliczana jest szerokość tekstu.
     * @return
     * @throws IOException
     */
    protected float getWidth(int fontSize) throws IOException {
        return PDType0Font.load(PDFGenerator.document, new File(font.getFileName())).getStringWidth(text) / 1000 * fontSize;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
}
