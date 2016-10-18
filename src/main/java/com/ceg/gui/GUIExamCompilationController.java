package com.ceg.gui;

import com.ceg.examContent.Exam;
import com.ceg.pdf.PDFSettings;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;

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
    private int taskIdx;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        saveButton.setDisable(true);
        progressUpdate();
     //   detailsUpdate();
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
            appStage.setOnHidden(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    GUIMainController.getInstance().updateWindow(Exam.getInstance().idx);
                }
            });
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
                Exam exam = Exam.getInstance();
                int progress= -1;
                int prevProgress = -2;       // progress i prevProgress na poczatku nie mogą być takie same, bo na starcie
                                             // nie zaktualizuje się progressBar
                while ((progress = exam.getCompilationProgress()) + 1 < exam.getTasks().size()) {   
                    if (cancelled == true) {
                        return null;
                    }
                    if(prevProgress == progress) continue;
                    updateProgress(progress + 1, exam.getTasks().size());
                    updateTitle("Trwa kompilacja zadania " + (progress + 2) + " z " + exam.getTasks().size() + "...");
                    updateMessage(String.join("", exam.getOutputList()));
                    prevProgress = progress;
                }
                updateProgress(1, 1);
                updateMessage("Kompilacja zakończona pomyślnie.");
                return null;
            }
        };
        taskNumberLabel.textProperty().bind(task.titleProperty());
        compilationDetails.textProperty().bind(task.messageProperty());
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
}
