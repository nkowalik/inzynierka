/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.gui;

import static com.ceg.gui.PdfSavingController.appStage;
import com.ceg.pdf.PDFSettings;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author marta
 */
public class AdvancedOptionsController implements Initializable{
     @FXML
    ChoiceBox commandFont;
    @FXML
    TextField commandFontSize;
    
    @FXML
    ChoiceBox codeFont;
    @FXML
    TextField codeFontSize;
    
    public static Stage appStage;
    private PDFSettings pdfSettings;
    
    private final List<String> fontList = Arrays.asList("Arial", "Courier","Times New Roman", "Verdana");
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        pdfSettings = PDFSettings.getInstance();
        
        commandFont.setItems(FXCollections.observableList(fontList));
        commandFont.setValue(pdfSettings.getCommandFont());
        
        codeFont.setItems(FXCollections.observableList(fontList));
        codeFont.setValue(pdfSettings.getCodeFont());
        
        commandFontSize.setText(pdfSettings.getCommandFontSize().toString());
        codeFontSize.setText(pdfSettings.getCodeFontSize().toString());
        
    }
    
    public static synchronized void show() throws IOException {
        if(appStage == null) {
            URL location = GUIMainController.class.getResource("/fxml/advancedOptions.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            
            Scene scene = new Scene((Pane)loader.load(location.openStream()));
            appStage = new Stage();
            appStage.setScene(scene);
            appStage.setTitle("Opcje zaawansowane");
            appStage.setResizable(false);
        }
        
        appStage.show();
        appStage.toFront();
    }
    
    public void apply(ActionEvent event) throws IOException {   
        PDFSettings.getInstance().setCommandFont(commandFont.getValue().toString());
        PDFSettings.getInstance().setCodeFont(codeFont.getValue().toString());
        
        PDFSettings.getInstance().setCommandFontSize(Integer.parseInt(commandFontSize.getText()));
        PDFSettings.getInstance().setCodeFontSize(Integer.parseInt(codeFontSize.getText()));
        
        appStage.hide();
    }
    
    public void cancel(ActionEvent event) {
        appStage.hide();
    }
}
