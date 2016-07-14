package com.ceg.pdf;

import com.ceg.examContent.Exam;
import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.gui.PdfSavingController;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 *
 * @author Martyna
 */
public class PDFSettings {
    private String commandFont;
    private String codeFont;
    private Integer commandFontSize;
    private Integer codeFontSize;
    private String testType;
    private String pdfFilePath;
    private String pdfFileName;
    
    private Integer year;
    private Integer month;
    private Integer day;
    
    private String date;
    private File pdfFile;
    
    public PDFSettings(String commandFont, String codeFont, Integer commandFontSize, Integer codeFontSize, String testType, String pdfFileName) {
        this.commandFont = commandFont;
        this.codeFont = codeFont;
        this.commandFontSize = commandFontSize;
        this.codeFontSize = codeFontSize;
        this.testType = testType;
        this.pdfFileName = pdfFileName;
        
        Calendar calendar = Calendar.getInstance();
        
        year = calendar.get(calendar.YEAR);
        month = calendar.get(calendar.MONTH) + 1;
        day = (Integer)calendar.get(calendar.DAY_OF_MONTH);
        
        File file = new File(".");
        pdfFilePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-2);        
    }
    
    public void setCommandFont(String commandFont) {
        this.commandFont = commandFont;
    }
    public void setCodeFont(String codeFont) {
        this.codeFont = codeFont;
    }
    public void setCommandFontSize(Integer commandFontSize) {
        this.commandFontSize = commandFontSize;
    }
    public void setCodeFontSize(Integer codeFontSize) {
        this.codeFontSize = codeFontSize;
    }
    public void setTestType(String testType) {
        this.testType = testType;
    }
    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }
    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    public void setMonth(Integer month) {
        this.month = month;
    }
    public void setDay(Integer day) {
        this.day = day;
    }
    
    public String getCommandFont() {
        return commandFont;
    }
    public String getCodeFont() {
        return codeFont;
    }
    public Integer getCommandFontSize() {
        return commandFontSize;
    }
    public Integer getCodeFontSize() {
        return codeFontSize;
    }
    public String getTestType() {
        return testType;
    }
    public String getPdfFileName() {
        return pdfFileName;
    }
    public String getPdfFilePath() {
        return pdfFilePath;
    }
    
    public Integer getYear() {
        return year;
    }
    public Integer getMonth() {
        return month;
    }
    public Integer getDay() {
        return day;
    }
    
    public File getPdfFile() {
        return pdfFile;
    }
    
    public void saveFile() {
        date = "";
        
        if (day < 10)
            date = "0";
        date += day.toString() + '.';
        if (month < 10)
            date += '0';
        date += month.toString() + '.' + year.toString();
        
        pdfFile = new File(pdfFilePath + '/' + pdfFileName);
    }
    
    public void pdfGenerate(Stage stage) {
        try {
            System.out.println(Exam.getInstance().getTaskAtIndex(0).getAnswers().size());
            System.out.println(Exam.getInstance().getTaskAtIndex(0).getPDFCode().size());
            System.out.println(Exam.getInstance().getTaskAtIndex(0).getContents().size());
            PDFGenerator gen = new PDFGenerator(    pdfFile,
                    commandFont,
                    commandFontSize,
                    codeFont,
                    codeFontSize,
                    date,
                    testType);
        } catch (IOException ex) {
            Logger.getLogger(PdfSavingController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmptyPartOfTaskException ex) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Błąd egzaminu");
            alert.setHeaderText("Sprawdź czy pola z poleceniem i kodem nie są puste oraz czy podana liczba odpowiedzi zgadza się ze stanem faktycznym.");
               
            alert.showAndWait();
        }
        stage.hide();
    }
}
