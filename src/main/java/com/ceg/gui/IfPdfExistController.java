package com.ceg.gui;

import com.ceg.pdf.PDFGenerator;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Martyna
 */
public class IfPdfExistController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    public void zapisz(ActionEvent event) {
        /*PDFGenerator gen = new PDFGenerator(    pdfFile, 
                                                commandFontName, 
                                                commandFontSizeNumber, 
                                                codeFontName, 
                                                codeFontSizeNumber, 
                                                date, 
                                                testTypeName); */
        PdfSavingController.pdfGenerate();
        
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
    }
    
    public void anuluj(ActionEvent event) {
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
    }
}
