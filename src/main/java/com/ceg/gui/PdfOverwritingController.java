package com.ceg.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Klasa reprezentująca kontroler okna do nadpisywania pliku.
 */
public class PdfOverwritingController implements Initializable {
    public static Stage appStage;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public static synchronized void show() throws IOException {
        if(appStage == null) {
            URL location = GUIMainController.class.getResource("/fxml/pdfOverwriting.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);

            Scene scene = new Scene((Pane)loader.load(location.openStream()));
            appStage = new Stage();
            appStage.setTitle("Czy chcesz nadpisać?");
            appStage.setScene(scene);
            appStage.setResizable(false);
            appStage.setAlwaysOnTop(true);
            appStage.show();
            appStage.setResizable(false);
        }
        
        appStage.show();
        appStage.toFront();
    }
    
    public void save(ActionEvent event) throws IOException {
        appStage.hide();
        GUIExamCompilationController.show();
    }

    /**
     * Zamyka okno nadpisywania pliku.
     * @param event
     */
    public void cancel(ActionEvent event) {
        appStage.hide();
    }
}
