package com.ceg.gui;

import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import com.ceg.examContent.TaskType;
import com.ceg.examContent.TaskTypeComplexOutput;
import com.ceg.examContent.TaskTypeLineNumbers;
import com.ceg.examContent.TaskTypeSimpleOutput;
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

import javax.swing.*;

/**
 *
 * @author Natalia
 */


public class GUIAddTaskController implements Initializable {

    ArrayList<String> contentList = new ArrayList<>();
    ArrayList<String> codeList = new ArrayList<>();
    TaskType type;
    @FXML
    TextArea text;
    @FXML
    TextArea code;
    @FXML
    Button finish;
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
    private static GUIAddTaskController addTaskInstance = null;
    private static GUIMainController mainInstance = null;
    private File lastPath;

    public static synchronized void show() throws IOException {
        if(stage == null) {
            URL location = GUIAddTaskController.class.getResource("/fxml/addTask.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);

            Scene scene = new Scene((Pane)loader.load(location.openStream()));

            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Dodaj nowe zadanie");
            stage.setResizable(false);
        }
        clearFields();
        stage.show();
        stage.toFront();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addTaskInstance = this;
        mainInstance = GUIMainController.getInstance();
    }
    public static GUIAddTaskController getInstance() {
        return addTaskInstance;
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
            finish.setDisable(false);
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }
    public void addTypeSimpleOutput() {
        chooseType.setText(taskTypeSimpleOutput.getText());
        mainInstance.setStageName("CEG - " + taskTypeSimpleOutput.getText());
        addType("simple_output.txt");
        type = new TaskTypeSimpleOutput();
    }
    public void addTypeReturnedValue() {
        chooseType.setText(taskTypeReturnedValue.getText());
        mainInstance.setStageName("CEG - " + taskTypeReturnedValue.getText());
        addType("returned_value.txt");
        type = new TaskTypeSimpleOutput(); // UNSUPPORTED YET
    }
    public void addTypeComplexOutput() {
        chooseType.setText(taskTypeComplexOutput.getText());
        mainInstance.setStageName("CEG - " + taskTypeComplexOutput.getText());
        addType("complex_output.txt");
        type = new TaskTypeComplexOutput();
    }
    public void addTypeGaps() {
        chooseType.setText(taskTypeGaps.getText());
        mainInstance.setStageName("CEG - " + taskTypeGaps.getText());
        addType("gaps.txt");
        type = new TaskTypeSimpleOutput(); // UNSUPPORTED YET
    }
    public void addTypeVarValue() {
        chooseType.setText(taskTypeVarValue.getText());
        mainInstance.setStageName("CEG - " + taskTypeVarValue.getText());
        addType("var_value.txt");
        type = new TaskTypeSimpleOutput(); // UNSUPPORTED YET
    }
    public void addTypeLineNumbers() {
        chooseType.setText(taskTypeLineNumbers.getText());
        mainInstance.setStageName("CEG - " + taskTypeLineNumbers.getText());
        addType("line_numbers.txt");
        type = new TaskTypeLineNumbers();
    }
    public void finishEdition(ActionEvent event) throws Exception {
        Task t = new Task(type);
        t.setContents(contentList);
        t.setCode(codeList);
        Exam.getInstance().addTask(t); // wrzuca na koniec listy, ustawia idx na size-1 (ostatni element)
        stage.hide();

        mainInstance.getInstance().addNewTabPaneTab();
        //mainInstance.getInstance().updateWindow(Exam.getInstance().idx);

        chooseType.setText("Typ zadania");
    }
    public static void clearFields() {
        addTaskInstance.text.clear();
        addTaskInstance.code.clear();
        addTaskInstance.codeList = new ArrayList<>();
        addTaskInstance.contentList = new ArrayList<>();
        addTaskInstance.finish.setDisable(true);
    }
    public void cancelEdition(ActionEvent event) throws Exception {
        mainInstance.setStageName("CEG");
        stage.hide();
    }
    public void selectCodeFile() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        if (lastPath != null) {
            fileChooser.setCurrentDirectory(lastPath);
        }
        int returnedVal = fileChooser.showOpenDialog(null);
        if (returnedVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            fileChooser.setCurrentDirectory(file);
            lastPath = fileChooser.getCurrentDirectory();
            if(file != null) {
                loadFile(file);
            }
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
        finish.setDisable(false);
    }
}
