package com.ceg.pdf;

import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/* klasa odpowiedzialna za generowanie pdfa. Aby utworzyć dokument należy utworzyć obiekt tej klasy za pomocą 
   konstruktora, który jako argumenty przyjmuje nazwę przyszłego pliku pdf, tekst polecenia i listę linii kodu */
public class PDFGenerator {
    private final PDDocument document;
    private PDPage actualPage = null;
    public static PDPageContentStream cs;
    
    private static final int commandWidth = 250;
    private static final int codeWidth = 250;
    private final static int leftMargin = 30;
    private final static int leftCodeMargin = leftMargin + commandWidth + leftMargin;
    private static final int topMargin = 760;
    private static final int bottomMargin = 40;
    private static final int breakBetweenTasks = 25;
    
    private static final PDFont font = PDType1Font.TIMES_ROMAN;
    private static final int fontSize = 12;
    
    private int actualY;
    
    /* wywołanie konstruktora powoduje utworzenie dokumentu pdf. Argumentem nazwa przyszłego pliku pdf */
    public PDFGenerator(String fileName) throws IOException {
        boolean newPage;
        
        //tworzenie dokumentu
        document = new PDDocument();
        
        //tworzenie nowej strony i dodanie jej do dokumentu
        createNewPage();
        
        PDFHeader header = new PDFHeader(new PDFLine(font, fontSize));
        actualY = header.setHeader(leftMargin, topMargin, breakBetweenTasks);

        List<Task> taskList = Exam.getInstance().getTasks();
        
        PDFCommand comm = new PDFCommand(commandWidth, PDType1Font.TIMES_ROMAN, 10);
        PDFCode code = new PDFCode(codeWidth, PDType1Font.COURIER, 10);
        
        for (Task i : taskList) {           
            comm.textSplitting(i.getContents());
            code.textSplitting(i.getPDFCode());
            
            //jeśli zadanie nie mieści się na stronie, to tworzymy nową stronę
            if (actualY - comm.getLineHeight()*comm.getNumberOfLines() < bottomMargin &&
                actualY - code.getLineHeight()*code.getNumberOfLines() < bottomMargin) {
                createNewPage();
                newPage = true;
            } 
            else {
                newPage = false;
            }
                
            int commandLines = comm.writeToPDF(leftMargin, actualY);
            int codeLines = code.writeToPDF(leftCodeMargin, actualY);
            
            actualY = (commandLines < codeLines) ? commandLines : codeLines; 
            
            if (!newPage) {
                actualY -= breakBetweenTasks;
            }
        }
        cs.close();
        savePDF(fileName);
    }
    
    /* Funkcja tworząca dokument pdf */
    private void savePDF(String fileName) throws IOException {
        File newFile = new File(fileName);
        document.save(newFile);
        document.close();
    }
    
    private void createNewPage() throws IOException {
        if (actualPage != null)
            cs.close();
        
        actualPage = new PDPage();
        document.addPage(actualPage);
        cs = new PDPageContentStream(document, actualPage);
        
        actualY = topMargin;
    }
}
