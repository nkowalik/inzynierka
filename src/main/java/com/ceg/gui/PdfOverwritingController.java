package com.ceg.gui;

import com.ceg.pdf.PDFSettings;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Klasa reprezentująca kontroler okna do nadpisywania pliku.
 */
public class PdfOverwritingController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Generuje plik .pdf nadpisując wcześniej istniejący (o tej samej nazwie).
     * @param event
     */
    public void save(ActionEvent event) {
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
        
        PDFSettings.getInstance().pdfGenerate(PdfSavingController.appStage);
    }

    /**
     * Zamyka okno nadpisywania pliku.
     * @param event
     */
    public void cancel(ActionEvent event) {
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
    }
}
