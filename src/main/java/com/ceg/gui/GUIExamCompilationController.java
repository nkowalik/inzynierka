package com.ceg.gui;

import com.ceg.examContent.Exam;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Martyna
 */
public class GUIExamCompilationController implements Initializable {
    @FXML
    ProgressBar progressBar;

    public static Stage appStage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        progressBar.setProgress(0);
        
        Task task = new Task() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                    progressBar.progressProperty().bind(new SimpleDoubleProperty(Exam.getInstance().getCompilationProgress()));
                    progressBar.progressProperty().unbind();
                } catch (InterruptedException ex) {
                    Logger.getLogger(GUIExamCompilationController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            protected Object call() throws Exception {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        new Thread(task).start();

    }
    
    public static synchronized void show() throws IOException {
        if(appStage == null) {
            URL location = GUIExamCompilationController.class.getResource("/fxml/examCompilation.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            
            Scene scene = new Scene((Pane)loader.load(location.openStream()));
            appStage = new Stage();
            appStage.setScene(scene);
            appStage.setTitle("Kompilacja egzaminu");
            appStage.setResizable(false);
        }
        
        appStage.show();
        appStage.toFront();
    }
}
