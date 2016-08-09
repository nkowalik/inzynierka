package com.ceg.gui;

import com.ceg.examContent.Exam;
import com.ceg.pdf.PDFSettings;
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
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
        
        PDFSettings.getInstance().pdfGenerate(PdfSavingController.appStage);
    }
    
    public void anuluj(ActionEvent event) {
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
    }
}
