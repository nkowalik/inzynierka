package com.ceg.pdf;

import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    /* wywołanie konstruktora powoduje utworzenie dokumentu pdf. Argumentem nazwa przyszłego pliku pdf */
    public PDFGenerator(File pdfFile, String commandFont, int commandFontSize, String codeFont, int codeFontSize, String testDate, String testType) throws IOException {
        document = new PDDocument();
        boolean newPage;
        //tworzenie nowej strony i dodanie jej do dokumentu
        createNewPage();
        
        PDFHeader header = new PDFHeader();
        actualY = header.setHeader(leftMargin, topMargin, breakBetweenTasks, testDate);

        List<Task> taskList = Exam.getInstance().getTasks();
        
        PDFCommand comm = new PDFCommand(commandWidth, commandFont, commandFontSize);
        PDFCode code = new PDFCode(codeWidth, codeFont, codeFontSize);
        PDFAnswer answer;
        
        if (Exam.getInstance().pdfSettings.getTestType().equals("student")) {
            answer = new PDFAnswer(commandWidth, commandFont, commandFontSize);
        }
        else {
            answer = new PDFTeachersAnswer(commandWidth, commandFont, commandFontSize);
        }
        Integer taskNumber = 1;
        for (Task i : taskList) {
            comm.setTaskNumber(taskNumber++);
            comm.textSplitting(i.getContents());
            code.textSplitting(i.getPDFCode());
            
            answer.textSplitting(i.getPDFAnswers());
            answer.setAnswers(i.getAnswers());
            //jeśli zadanie nie mieści się na stronie, to tworzymy nową stronę
            if (actualY - comm.getLineHeight()*comm.getNumberOfLines() - answer.getLineHeight()*answer.getNumberOfLines() < bottomMargin  ||
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
        
        savePDF(pdfFile);
    }
    
    /* Funkcja tworząca dokument pdf */
    private void savePDF(File pdfFile) throws IOException {
        document.save(pdfFile);
        document.close();
        Desktop desktop = Desktop.getDesktop();
        EventQueue EQ = new EventQueue();
        if(desktop.isSupported(Desktop.Action.OPEN)){
             EQ.invokeLater(() -> {
                 try {
                     desktop.open(pdfFile);
                 } catch (IOException ex) {
                     Logger.getLogger(PDFGenerator.class.getName()).log(Level.SEVERE, null, ex);
                 }
             });
        }
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
