package com.ceg.gui;

import com.ceg.pdf.PDFGenerator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
    TextField dateDay;
    @FXML
    ChoiceBox dateMonth;
    @FXML
    TextField dateYear;
    
    private final List<String> fontList = Arrays.asList("Arial", "Arial Narrow", "Courier","Times New Roman", "Verdana");
    private final List<String> testTypeList = Arrays.asList("student", "nauczyciel", "interaktywny");
    private final List<String> monthList = new ArrayList<>();
    
    private String commandFontName;
    private String codeFontName;
    private Integer commandFontSizeNumber;
    private Integer codeFontSizeNumber;
    private String testTypeName;
    private String date; 
    private String filePathName;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        dateMonth.setValue(monthList.get(0));
        
        commandFontSize.setText("10");
        codeFontSize.setText("10");
        
        dateDay.setText("15");
        dateYear.setText("2017");
        
        File file = new File(".");
        String path = file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-1);
        filePath.setText(path + "plik.pdf");
    }
    
    public void saveFile(ActionEvent event) {
        commandFontName = commandFont.getValue().toString();
        codeFontName = codeFont.getValue().toString();
        
        commandFontSizeNumber = Integer.parseInt(commandFontSize.getText());
        codeFontSizeNumber = Integer.parseInt(codeFontSize.getText());
        
        testTypeName = testType.getValue().toString();
        
        date = dateDay.getText() + '.' + dateMonth.getValue().toString() + '.' + dateYear.getText();
        
        filePathName = filePath.getText();
        
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
