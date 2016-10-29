package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.utils.Alerts;
import com.ceg.gui.PdfSavingController;
import com.ceg.utils.FontType;
import com.ceg.utils.FontTypeUtil;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martyna
 */
public class PDFSettings {
    private final Properties defaultSettings = new Properties();
    
    private FontType commandFont;
    private FontType codeFont;
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

    public static int commandWidth;
    public static int codeWidth;
    public static int leftMargin;
    public static int leftCodeMargin;
    public static int topMargin;
    public static int bottomMargin;
    public static int breakBetweenTasks;

    private static final PDFSettings instance = new PDFSettings();

    public PDFSettings() {       
        preparePropertiesInput();
        
        commandWidth = Integer.parseInt(defaultSettings.getProperty("command.width"));
        codeWidth = Integer.parseInt(defaultSettings.getProperty("code.width"));
        leftMargin = Integer.parseInt(defaultSettings.getProperty("left.margin"));
        topMargin = Integer.parseInt(defaultSettings.getProperty("top.margin"));
        bottomMargin = Integer.parseInt(defaultSettings.getProperty("bottom.margin"));
        breakBetweenTasks = Integer.parseInt(defaultSettings.getProperty("break.between.tasks"));
        
        leftCodeMargin = leftMargin + commandWidth + leftMargin/2;
        
        this.commandFont = FontTypeUtil.change(defaultSettings.getProperty("command.font"));
        this.codeFont = FontTypeUtil.change(defaultSettings.getProperty("code.font"));
        this.commandFontSize = Integer.parseInt(defaultSettings.getProperty("command.font.size"));
        this.codeFontSize = Integer.parseInt(defaultSettings.getProperty("code.font.size"));
        this.testType = defaultSettings.getProperty("test.type");
        this.pdfFileName = defaultSettings.getProperty("pdf.file.name");
        
        Calendar calendar = Calendar.getInstance();        
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        
        File file = new File(".");
        pdfFilePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-2);       
    }
    
    private void preparePropertiesInput() {
	String propFileName = "properties/pdfSettings.properties";
 
	InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
        try {
            if (inputStream != null) {
                defaultSettings.load(inputStream);
            } 
            else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (Exception ex) {
            Logger.getLogger(PDFSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setCommandFont(FontType commandFont) {
        this.commandFont = commandFont;
    }
    public void setCodeFont(FontType codeFont) {
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
    
    public FontType getCommandFont() {
        return commandFont;
    }
    public FontType getCodeFont() {
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

    public String getDate() {
        return date;
    }
    
    public File getPdfFile() {
        return pdfFile;
    }
    public void setPdfFile(File pdfFile) { this.pdfFile = pdfFile; }

    public void formatDate() {
        date = "";
        
        if (day < 10)
            date = "0";
        date += day.toString() + '.';
        if (month < 10)
            date += '0';
        date += month.toString() + '.' + year.toString();
    }
    
    public void pdfGenerate(Stage stage) throws IOException {
        try {
            PDFGenerator gen = new PDFGenerator();
        } catch (IOException ex) {
            Alerts.fileAlreadyOpened();
            PDFGenerator.document.close();
        } catch (EmptyPartOfTaskException ex) {
            Alerts.emptyPartOfTaskAlert();
        }
        stage.hide();
    }

    public static PDFSettings getInstance() {
        return instance;
    }
}
