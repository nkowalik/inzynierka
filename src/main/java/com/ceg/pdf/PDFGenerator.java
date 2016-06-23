package com.ceg.pdf;

import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

/* klasa odpowiedzialna za generowanie pdfa. Aby utworzyć dokument należy utworzyć obiekt tej klasy za pomocą 
   konstruktora, który jako argumenty przyjmuje nazwę przyszłego pliku pdf, tekst polecenia i listę linii kodu */
public class PDFGenerator {
    public static PDDocument document;
    private PDPage actualPage = null;
    public static PDPageContentStream cs;
    
    private static final int commandWidth = 250;
    private static final int codeWidth = 250;
    private final static int leftMargin = 30;
    private final static int leftCodeMargin = leftMargin + commandWidth + leftMargin;
    private static final int topMargin = 760;
    private static final int bottomMargin = 40;
    private static final int breakBetweenTasks = 25;   
    
    private int actualY;
    
    private String testDate;
    
    /* wywołanie konstruktora powoduje utworzenie dokumentu pdf. Argumentem nazwa przyszłego pliku pdf */
    public PDFGenerator(String fileName, String commandFont, int commandFontSize, String codeFont, int codeFontSize, String testDate, String testType) throws IOException {
        document = new PDDocument();
        boolean newPage;
        
        //tworzenie nowej strony i dodanie jej do dokumentu
        createNewPage();
        
        this.testDate = testDate;
        
        PDFHeader header = new PDFHeader();
        actualY = header.setHeader(leftMargin, topMargin, breakBetweenTasks, testDate);

        List<Task> taskList = Exam.getInstance().getTasks();
        
        PDFCommand comm = new PDFCommand(commandWidth, commandFont, commandFontSize);
        PDFCode code = new PDFCode(codeWidth, codeFont, codeFontSize);
        PDFAnswer answer = new PDFAnswer(commandWidth, commandFont, commandFontSize);
        
        for (Task i : taskList) {           
            comm.textSplitting(i.getContents());
            code.textSplitting(i.getPDFCode());
            answer.textSplitting(i.getPDFAnswers());
            
            //jeśli zadanie nie mieści się na stronie, to tworzymy nową stronę
            if (actualY - comm.getLineHeight()*(comm.getNumberOfLines() + answer.getNumberOfLines()) < bottomMargin  &&
                actualY - code.getLineHeight()*code.getNumberOfLines() < bottomMargin) {
                createNewPage();
                newPage = true;
            } 
            else {
                newPage = false;
            }
                
            int commandLines = comm.writeToPDF(leftMargin, actualY);
            int codeLines = code.writeToPDF(leftCodeMargin, actualY);
            commandLines = answer.writeToPDF(leftMargin, commandLines);
            
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
