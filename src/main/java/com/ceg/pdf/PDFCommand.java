package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import java.io.IOException;
import java.util.List;

public class PDFCommand extends PDFAbstractTaskPart {
    protected String[] words;
    protected String line = "";
    protected final float spaceWidth;
    private Integer taskNumber;
        
    PDFCommand(List<String> lines, int taskNumber) throws IOException, EmptyPartOfTaskException {
        super();
        this.taskNumber = taskNumber;
        PDFSettings pdfSettings = PDFSettings.getInstance();
        textWidth = pdfSettings.commandWidth;
        pdfLine = new PDFLine(pdfSettings.getCommandFont(), pdfSettings.getCommandFontSize());
        leftMargin = pdfSettings.leftMargin;
        spaceWidth = getWidth(" ");
        textSplitting(lines);
    }
    
    /*  Funkcja odpowiedzialna za formatowanie tekstu polecenia. Argumentem jest tekst polecenia.
        Dzieli wyrazy po spacji i układa jak najwięcej w jednej linii. Wrażliwa na znaki entera:
        każdy enter w poleceniu będzie widoczny w dokumencie pdf. Brak możliwości używania innych
        białych znaków poza spacją i enterem. */    
    @Override
    public void textSplitting (List<String> command) throws IOException, EmptyPartOfTaskException {
        super.textSplitting(command);
	    line = "";
        actualWidth = 0;       
	    String string = mergeStringList(command);
        string = taskNumber.toString() + ". " + string;
        string = string.replace("\n", " ");
        float actualWordWidth;
        words = string.split(" ");        
        
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
        actualTaskLines.add("");
    } 
    
    private String mergeStringList(List<String> command) {
        String txt = new String();
        // pobranie treści polecenia w formie listy, sklejenie jej w jednego Stringa      
        for (int index = 0 ; index < command.size(); index++) {
            String line = command.get(index);
            txt+=line+"\n";
        }
        return txt;
    }
}
