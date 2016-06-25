package com.ceg.gui;

import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import com.ceg.gui.GUIMainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

/**
 *
 * @author Natalia
 */


public class GUIAddTaskController implements Initializable {
    
    ArrayList<String> contentList = new ArrayList<>();
    ArrayList<String> codeList = new ArrayList<>();
    @FXML
    TextArea text;
    @FXML
    TextArea code;
    
    private static Stage stage = null;
    private static GUIAddTaskController instance = null;

    public static synchronized void show() throws IOException {
        if(stage == null) {
            URL location = GUIAddTaskController.class.getResource("/fxml/addTask.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            
            Scene scene = new Scene((Pane)loader.load(location.openStream()));
            
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Dodaj nowe zadanie");
        }
        clearFields();
        stage.show();
        stage.toFront();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
    }
    public static GUIAddTaskController getInstance() {
        return instance;
    }
    public void addType1() {
        try {
            Scanner s = new Scanner(new File("polecenie1.txt"));
            contentList.clear();
            while(s.hasNext()) {
                contentList.add(s.nextLine());
            }
            s.close();
            text.clear();
            for (String i : contentList) {
                text.appendText(i + "\n");
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }

        // TODO
    }
    public void addType2() {
        try {
            Scanner s = new Scanner(new File("polecenie2.txt"));
            contentList.clear();
            while (s.hasNext()) {
                contentList.add(s.nextLine());
            }
            s.close();
            text.clear();
            for (String i : contentList) {
                text.appendText(i + "\n");
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }

        // TODO
    }
    public void finishEdition(ActionEvent event) throws Exception {
        Task t = new Task();
        t.setContents(contentList);
        t.setCode(codeList);
        Exam.getInstance().addTask(t);
        stage.hide();
        
        GUIMainController.getInstance().updateWindow();
        
    }
    public static void clearFields() {
        GUIAddTaskController.getInstance().text.clear();
        GUIAddTaskController.getInstance().code.clear();
        GUIAddTaskController.getInstance().codeList = new ArrayList<>();
        GUIAddTaskController.getInstance().contentList = new ArrayList<>();
    }
    public void cancelEdition(ActionEvent event) throws Exception {
        stage.hide();
    }
    public void selectCodeFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if(file != null) {
            loadFile(file);
        }
    }
    public void loadFile(File file) throws IOException {
        Scanner s = new Scanner(file);
        codeList.clear();
        while(s.hasNext()) {
            codeList.add(s.nextLine());
        }
        s.close();
        code.clear();
        for (String i : codeList) {
            code.appendText(i + "\n");
        }
    }
}
