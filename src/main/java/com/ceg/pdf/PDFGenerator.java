package com.ceg.pdf;

import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import com.ceg.exceptions.EmptyPartOfTaskException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * klasa odpowiedzialna za generowanie pdfa. Aby utworzyć dokument należy utworzyć obiekt tej klasy za pomocą konstruktora
 */
public class PDFGenerator {
    public static PDDocument document;
    private PDPage actualPage = null;
    public static PDPageContentStream cs;
    private static final int topMargin = PDFSettings.getInstance().topMargin;
    private static final int bottomMargin = PDFSettings.getInstance().bottomMargin;

    private PDFCommand comm;
    private PDFCode code;
    private PDFAnswer answer;

    private int actualY;
    
    /* wywołanie konstruktora powoduje utworzenie dokumentu pdf. Argumentem nazwa przyszłego pliku pdf */
    public PDFGenerator() throws IOException, EmptyPartOfTaskException {
        int breakBetweenTasks = PDFSettings.breakBetweenTasks;

        //tworzenie dokumentu, pierwszej strony i dodanie jej do dokumentu
        document = new PDDocument();
        createNewPage();
        
        PDFHeader header = new PDFHeader();
        actualY = header.setHeader(breakBetweenTasks);

        List<Task> taskList = Exam.getInstance().getTasks();
        
        Integer taskNumber = 1;
        for (Task i : taskList) {
            if (i.getText().getPDFCode().isEmpty() || i.getContent().getContentParts().size() <= 0 || i.getAnswers().size() < i.getPdfAnswers().size())
                throw new EmptyPartOfTaskException();
            
            comm = new PDFCommand(i.getContent(), taskNumber++);
            createCodeAndAnswer(i);

            answer.setAnswers(i.getAnswers());
            code.setAnswer(answer);

            writeTaskToPdf();
            actualY -= breakBetweenTasks;
        }
        cs.close();
        savePDF(PDFSettings.getInstance().getPdfFile());
    }
    
    /* Funkcja tworząca dokument pdf */
    private void savePDF(File pdfFile) throws IOException {
        document.save(pdfFile);
        document.close();
        Desktop desktop = Desktop.getDesktop();
        EventQueue EQ = new EventQueue();
        if(desktop.isSupported(Desktop.Action.OPEN)){
             EventQueue.invokeLater(() -> {
                 try {
                     desktop.open(pdfFile);
                 } catch (IOException ex) {

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

    private void writeTaskToPdf() throws IOException, EmptyPartOfTaskException {
        if (actualY - comm.getLineHeight()*comm.getNumberOfLines() -
                answer.getLineHeight()*answer.getNumberOfLines() < bottomMargin  ||
                actualY - code.getLineHeight()*code.getNumberOfLines() < bottomMargin) {
            createNewPage();
        }

        int commandLines = comm.writeToPDF(actualY);
        int codeLines = code.writeToPDF(actualY);
        
        if (answer.getNumberOfLines() > 0)
            commandLines = answer.writeToPDF(commandLines);

        actualY = (commandLines < codeLines) ? commandLines : codeLines;
    }

    private void createCodeAndAnswer(Task task) throws IOException, EmptyPartOfTaskException {
        switch(PDFSettings.getInstance().getTestType()) {
            case "nauczyciel":
                if (task.getType().name.equals("Gaps")) {
                    code = new PDFTeachersGapsCode(task.getText().getPDFCode());
                    answer = code.answer;
                }
                else if (task.getType().name.equals("LineNumbers")) {
                    code = new PDFLineNumbersCode(task.getText().getPDFCode());
                    answer = new PDFTeachersAnswer(task.getPdfAnswers());
                }
                else {
                    code = new PDFCode(task.getText().getPDFCode());
                    answer = new PDFTeachersAnswer(task.getPdfAnswers());
                }
                break;
            case "interaktywny":
                if (task.getType().name.equals("Gaps")) {
                    
                }
                break;
            default:
                    switch (task.getType().name) {
                        case "Gaps":
                            code = new PDFGapsCode(task.getText().getPDFCode());
                            answer = code.answer;
                            break;
                        case "LineNumbers":
                            code = new PDFLineNumbersCode(task.getText().getPDFCode());
                            answer = new PDFAnswer(task.getPdfAnswers());
                            break;
                        default:
                            code = new PDFCode(task.getText().getPDFCode());
                            answer = new PDFAnswer(task.getPdfAnswers());
                            break;
                    }
                break;
        }
        
    }
}
