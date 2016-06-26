package com.ceg.gui;

import com.ceg.pdf.PDFGenerator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Martyna
 */
public class PdfSavingController implements Initializable {
    @FXML
    ChoiceBox commandFont;
    @FXML
    TextField commandFontSize;
    
    @FXML
    ChoiceBox codeFont;
    @FXML
    TextField codeFontSize;
    
    @FXML
    ChoiceBox testType;
    @FXML
    TextField filePath;
    @FXML
    ChoiceBox dateDay;
    @FXML
    ChoiceBox dateMonth;
    @FXML
    ChoiceBox dateYear;
    @FXML
    TextField fileName;
    
    private final List<String> fontList = Arrays.asList("Arial", "Arial Narrow", "Courier","Times New Roman", "Verdana");
    private final List<String> testTypeList = Arrays.asList("student", "nauczyciel", "interaktywny");
    private final List<String> monthList = new ArrayList<>();
    private final List<String> yearList = new ArrayList<>();
    
    private final ObservableList<String> daysList = FXCollections.observableList(new ArrayList<String>());
    
    private String commandFontName;
    private String codeFontName;
    private Integer commandFontSizeNumber;
    private Integer codeFontSizeNumber;
    private String testTypeName;
    private String date; 
    private String filePathName;
    
    private Integer year;
    private Integer month;
    private Integer day;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Calendar calendar = Calendar.getInstance();
        
        for (Integer i = 1; i <= 12; i++) {
            if (i < 10) {
                monthList.add('0' + i.toString());
            }
            else {
                monthList.add(i.toString());
            }
        }
        commandFont.setItems(FXCollections.observableList(fontList));
        commandFont.setValue("Times New Roman");
        
        codeFont.setItems(FXCollections.observableList(fontList));
        codeFont.setValue("Courier");
        
        testType.setItems(FXCollections.observableList(testTypeList));
        testType.setValue("student");
        
        dateMonth.setItems(FXCollections.observableList(monthList));
        month = calendar.get(calendar.MONTH) + 1;
        if (month < 10)
            dateMonth.setValue('0' + month.toString());
        else
            dateMonth.setValue(month.toString());
        
        commandFontSize.setText("10");
        codeFontSize.setText("10");
        
        year = calendar.get(calendar.YEAR);
        for (Integer i  = year; i <= year+10; i++) {
            yearList.add(i.toString());
        }
        dateYear.setItems(FXCollections.observableList(yearList));
        dateYear.setValue(year.toString());
        
        YearMonth yM = YearMonth.of(year, month);
        Integer days = yM.lengthOfMonth();
        for (Integer i = 1; i <= days; i++) {
            daysList.add(i.toString());
        }
        day = (Integer)calendar.get(calendar.DAY_OF_MONTH);
        dateDay.setItems(daysList);
        dateDay.setValue(day.toString());
        
        File file = new File(".");
        String path = file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-2);
        filePath.setText(path);
        fileName.setText("egzamin.pdf");
        
        dateMonth.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                daysList.clear();
                month = newValue.intValue() + 1;
                YearMonth yM = YearMonth.of(year, month);
                Integer days = yM.lengthOfMonth();
                for (Integer i = 1; i <= days; i++) {
                    daysList.add(i.toString());
                }
                dateDay.setValue(day.toString());
            }
        });
        
        dateDay.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() != -1)
                    day = Integer.parseInt(daysList.get(newValue.intValue()));
            }           
        });
        
        dateYear.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                year = Integer.parseInt(yearList.get(newValue.intValue()));
                
                daysList.clear();
                
                YearMonth yM = YearMonth.of(year, month);
                Integer days = yM.lengthOfMonth();
                for (Integer i = 1; i <= days; i++) {
                    daysList.add(i.toString());
                }
                dateDay.setValue(day.toString());
            }           
        });
    }
    
    public void saveFile(ActionEvent event) {
        commandFontName = commandFont.getValue().toString();
        codeFontName = codeFont.getValue().toString();
        
        commandFontSizeNumber = Integer.parseInt(commandFontSize.getText());
        codeFontSizeNumber = Integer.parseInt(codeFontSize.getText());
        
        testTypeName = testType.getValue().toString();
        
        date = "";
        
        if (day < 10)
            date = "0";
        date += day.toString() + '.';
        if (month < 10)
            date += '0';
        date += month.toString() + '.' + year.toString();
        
        filePathName = filePath.getText() + '/' + fileName.getText();
        
        try {
            PDFGenerator gen = new PDFGenerator(    filePathName, 
                                                    commandFontName, 
                                                    commandFontSizeNumber, 
                                                    codeFontName, 
                                                    codeFontSizeNumber, 
                                                    date, 
                                                    testTypeName); 
        } 
        catch (IOException ex) {
            Logger.getLogger(PdfSavingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
    }
    
    public void cancel(ActionEvent event) {
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
    }
    
    public void browse(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser () ;
        dirChooser.setTitle("Wybierz lokalizację pliku");
        File dir = dirChooser.showDialog(null);
        
        if (dir != null) {
            filePath.setText(dir.getAbsolutePath());
        }
    }
}
