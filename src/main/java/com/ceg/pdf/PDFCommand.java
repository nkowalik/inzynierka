package com.ceg.pdf;

import com.ceg.examContent.Content;
import com.ceg.examContent.ContentPart;
import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.utils.ContentCssClass;
import com.ceg.utils.FontType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFCommand extends PDFAbstractTaskPart {
    protected String[] words;
    protected float spaceWidth;
    private final Integer taskNumber;
        
    PDFCommand(Content content, int taskNumber) throws IOException, EmptyPartOfTaskException {
        super();
        this.taskNumber = taskNumber;
        PDFSettings pdfSettings = PDFSettings.getInstance();
        textWidth = pdfSettings.commandWidth;
        fontSize = pdfSettings.getCommandFontSize();
        defaultFontType = pdfSettings.getCommandFont();
        leftMargin = pdfSettings.leftMargin;
        contentSplitting(content.getContentParts());
    }
    
    /*  Funkcja odpowiedzialna za formatowanie tekstu polecenia. Argumentem jest tekst polecenia.
        Dzieli wyrazy po spacji i układa jak najwięcej w jednej linii. Wrażliwa na znaki entera:
        każdy enter w poleceniu będzie widoczny w dokumencie pdf. Brak możliwości używania innych
        białych znaków poza spacją i enterem. */    
    public void contentSplitting (List<ContentPart> contentParts) throws IOException, EmptyPartOfTaskException {
        List<ContentPart> command = new ArrayList<ContentPart>(contentParts);
        //sprawdzić czy niepusty
        actualWidth = 0;       
       // String line = "";
        command.add(0, new ContentPart(ContentCssClass.EMPTY, taskNumber.toString() + ". "));
        //string = string.replace("\n", " ");
        float actualWordWidth;
        
        PDFLine pdfLine = new PDFLine(fontSize, leftMargin);
        
        for (ContentPart cp : command) {
            FontType ft = defaultFontType.contentCssClassToFontType(cp.getCssClassName());
            PDFLinePart lp = new PDFLinePart(ft);
            spaceWidth = getWidth(" ", ft, fontSize);
            words = cp.getText().split(" ");
            
            for (String word : words) {
                actualWordWidth = getWidth(word, ft, fontSize);
                
                 //linia wystarczająco długa, żaden wyraz więcej się nie zmieści
                if (actualWidth + actualWordWidth + spaceWidth  >= textWidth) {
                    pdfLine.getLineParts().add(lp);
                    pdfLines.add(pdfLine);
                    lp = new PDFLinePart(ft);
                    pdfLine = new PDFLine(fontSize, leftMargin);
                    lp.setText(word);
                    
                    actualWidth = actualWordWidth;
                }
                //do linii dopisujemy wyraz
                else {
                    //jeśli linia nie jest pusta to dodajemy spację po poprzednim wyrazie
                    if (!lp.getText().equals("")) {
                        lp.setText(lp.getText() + ' ');
                        actualWidth += spaceWidth;
                    }
                    lp.setText(lp.getText() + word);
                    actualWidth += actualWordWidth;
                }
            }
            //jeśli linia nie jest pusta, wypisujemy ją
            if (!lp.getText().equals("")) {
                lp.setText(lp.getText() + ' ');
                pdfLine.getLineParts().add(lp);
            }
        }
        pdfLines.add(pdfLine);
        pdfLines.add(new PDFLine(12, leftMargin));
    } 
}
