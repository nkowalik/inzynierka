package com.ceg.gui;

import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.stage.Stage;


/**
 *
 * @author Natalia
 */

public class GUIFirstPageController implements Initializable, Observer {
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Exam.getInstance().init();
        Exam.getInstance().addObserver(this);
        
    }
    
    public void createNewExam(ActionEvent event) throws Exception {
        Parent firstPage = FXMLLoader.load(getClass().getResource("/fxml/secondPage.fxml"));
        Scene firstScene = new Scene (firstPage);
        
        firstScene.getStylesheets().add("/styles/Styles.css");
        
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
        appStage.setScene(firstScene);
        appStage.show();
    }
    
    public void addTask(ActionEvent event) throws Exception {
        Stage addingTaskStage = new Stage();
        Parent scene = FXMLLoader.load(getClass().getResource("/fxml/addTask.fxml"));
        addingTaskStage.setTitle("Nowe zadanie");
        addingTaskStage.setScene(new Scene(scene, 650, 480));
        addingTaskStage.show();

    }
    
    
    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof ArrayList) {
            ArrayList<Task> list = (ArrayList)arg;
            System.out.println("working");
            try {
                Parent firstPage = FXMLLoader.load(getClass().getResource("/fxml/secondPage.fxml"));
                Scene firstScene = new Scene (firstPage);

                firstScene.getStylesheets().add("/styles/Styles.css");
                Stage appStage = GUIMain.getStage();

                appStage.hide();
                appStage.setScene(firstScene);
                appStage.show();
            } catch (IOException ex) {
                Logger.getLogger(GUIFirstPageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
}
