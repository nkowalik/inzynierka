package com.ceg.gui;

import com.ceg.examContent.Exam;
import com.ceg.pdf.PDFSettings;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
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
    
    @FXML
    Label taskNumberLabel;
    
    @FXML
    TextArea compilationDetails;
    
    @FXML
    Button saveButton;

    public static Stage appStage;
    private boolean cancelled = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        saveButton.setDisable(true);
        progressUpdate();
        detailsUpdate();
        examCompile();
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
    
    public void save(ActionEvent event) throws IOException {
        appStage.hide();
        PdfSavingController.appStage.hide();
        PDFSettings.getInstance().pdfGenerate(PdfSavingController.appStage);
    }
    
    public void cancel(ActionEvent event) {
        cancelled = true;
        appStage.hide();
        PdfSavingController.appStage.hide();
    }
    
    private void progressUpdate() {
        final Task<Void> task = new Task<Void>() {
            @Override 
            public Void call() throws Exception {
                while (Exam.getInstance().getCompilationProgress() + 1 < Exam.getInstance().getTasks().size()) {   
                    if (cancelled == true) {
                        return null;
                    }                    
                    updateProgress(Exam.getInstance().getCompilationProgress()+1, Exam.getInstance().getTasks().size());
                    updateMessage("Trwa kompilacja zadania " + (Exam.getInstance().getCompilationProgress()+2) + " z " + Exam.getInstance().getTasks().size() + "...");
                }
                updateProgress(1, 1);
                updateMessage("Kompilacja zakończona pomyślnie.");
                return null;
            }
        };
        taskNumberLabel.textProperty().bind(task.messageProperty());
        progressBar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }
    
    private void examCompile() {
        final Task<Void> task = new Task<Void>() {
            @Override 
            public Void call() throws Exception {
                saveButton.setDisable(!Exam.getInstance().compile());
                return null;
            }
        };
        new Thread(task).start();
    }
    
    private void detailsUpdate() {
        final Task<Void> task = new Task<Void>() {
            @Override 
            public Void call() throws Exception {
                while (Exam.getInstance().getCompilationProgress()+1 <= Exam.getInstance().getTasks().size()) {
                    if (cancelled) {
                        return null;
                    }
                    updateMessage(String.join("", Exam.getInstance().getOutputList()));
                }
                updateMessage(String.join("", Exam.getInstance().getOutputList()));
                return null;
            }
        };
        compilationDetails.textProperty().bind(task.messageProperty());
        new Thread(task).start();
    }
}
