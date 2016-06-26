package com.ceg.gui;

import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javafx.scene.control.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
    @FXML
    Menu chooseType;
    @FXML
    MenuItem taskTypeSimpleOutput;
    @FXML
    MenuItem taskTypeReturnedValue;
    @FXML
    MenuItem taskTypeComplexOutput;
    @FXML
    MenuItem taskTypeGaps;
    @FXML
    MenuItem taskTypeVarValue;
    @FXML
    MenuItem taskTypeLineNumbers;
    
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

    public void addType(String sTxt) {
        try {
            Scanner s = new Scanner(new File(sTxt));
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
    }
    public void addTypeSimpleOutput() {
        chooseType.setText(taskTypeSimpleOutput.getText());
        GUIMainController.setStageName("CEG - " + taskTypeSimpleOutput.getText());
        addType("simple_output.txt");
    }
    public void addTypeReturnedValue() {
        chooseType.setText(taskTypeReturnedValue.getText());
        GUIMainController.setStageName("CEG - " + taskTypeReturnedValue.getText());
        addType("returned_value.txt");
    }
    public void addTypeComplexOutput() {
        chooseType.setText(taskTypeComplexOutput.getText());
        GUIMainController.setStageName("CEG - " + taskTypeComplexOutput.getText());
        addType("complex_output.txt");
    }
    public void addTypeGaps() {
        chooseType.setText(taskTypeGaps.getText());
        GUIMainController.setStageName("CEG - " + taskTypeGaps.getText());
        addType("gaps.txt");
    }
    public void addTypeVarValue() {
        chooseType.setText(taskTypeVarValue.getText());
        GUIMainController.setStageName("CEG - " + taskTypeVarValue.getText());
        addType("var_value.txt");
    }
    public void addTypeLineNumbers() {
        chooseType.setText(taskTypeLineNumbers.getText());
        GUIMainController.setStageName("CEG - " + taskTypeLineNumbers.getText());
        addType("line_numbers.txt");
    }
    public void finishEdition(ActionEvent event) throws Exception {
        Task t = new Task();
        t.setContents(contentList);
        t.setCode(codeList);
        Exam.getInstance().addTask(t);
        stage.hide();
        
        GUIMainController.getInstance().addNewTabPaneTab();
        GUIMainController.getInstance().updateWindow();
        
    }
    public static void clearFields() {
        GUIAddTaskController.getInstance().text.clear();
        GUIAddTaskController.getInstance().code.clear();
        GUIAddTaskController.getInstance().codeList = new ArrayList<>();
        GUIAddTaskController.getInstance().contentList = new ArrayList<>();
    }
    public void cancelEdition(ActionEvent event) throws Exception {
        GUIMainController.setStageName("CEG");
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
