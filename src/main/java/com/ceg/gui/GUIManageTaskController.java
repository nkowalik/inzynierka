package com.ceg.gui;

import com.ceg.examContent.*;
import com.ceg.xml.TaskData;
import com.ceg.xml.TasksLoading;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javafx.scene.control.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;

/**
 * Klasa reprezentująca kontroler okna dodawania zadania.
 */
public class GUIManageTaskController implements Initializable {

    ArrayList<String> contentList = new ArrayList<>();
    ArrayList<String> codeList = new ArrayList<>();
    TaskType type;
    @FXML
    TextArea text;
    @FXML
    CodeArea code;
    @FXML
    Button finish;
    @FXML
    Button loadCode;
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
    private static GUIManageTaskController manageTaskInstance = null;
    private static GUIMainController mainInstance = null;
    private FileChooser fileChooser;

    /**
     * Wyświetla okno dodawania zadania.
     * @throws IOException
     */
    public static synchronized void show(String action) throws IOException {
        if(stage == null) {
            URL location = GUIManageTaskController.class.getResource("/fxml/manageTask.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);

            Scene scene = new Scene((Pane)loader.load(location.openStream()));

            stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
        }
        clearFields();
        if (action.equals("add")) {
            stage.setTitle("Dodaj nowe zadanie");
            //GUIManageTaskController.getInstance().loadCode.setDisable(false);
        }
        else if (action.equals("edit")) {
            stage.setTitle("Edycja zadania");
            //GUIManageTaskController.getInstance().loadCode.setDisable(true);
        }
        stage.show();
        stage.toFront();
        GUIManageTaskController.getInstance().finish.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        manageTaskInstance = this;
        fileChooser = new FileChooser();
        mainInstance = GUIMainController.getInstance();
    }
    public static GUIManageTaskController getInstance() {
        return manageTaskInstance;
    }

    /**
     * Wpisuje do okna edycji zadania kod i polecenie z okna głównego.
     * @param task Zadanie pobrane z okna głównego.
     */
    public void editTask(Task task) {
        updateText(task.getContents());
        task.getText().createCodeAreaText(code);
        finish.setDisable(false);
    }

    /**
     * Wpisuje do okna treść polecenia dla wybranego typu zadania.
     * @param index Numer wybranego zadania.
     */
    public void addType(int index) {
        TaskData tasks = TasksLoading.loadFromXml();

        contentList.clear();
        text.clear();
        if (tasks != null) {
            contentList.add(tasks.getTaskData().get(index).getText());
            text.appendText(tasks.getTaskData().get(index).getText());
        }

        finish.setDisable(false);
    }

    /**
     * Ustawia typ wybranego zadania, zmienia nagłówek okna dodawania zadania na odpowiadający wybranemu typowi.
     */
    public void addTypeSimpleOutput() {
        chooseType.setText(taskTypeSimpleOutput.getText());
        mainInstance.setStageName("CEG - " + taskTypeSimpleOutput.getText());
        addType(0);
        type = new TaskTypeSimpleOutput();
    }
    public void addTypeReturnedValue() {
        chooseType.setText(taskTypeReturnedValue.getText());
        mainInstance.setStageName("CEG - " + taskTypeReturnedValue.getText());
        addType(1);
        type = new TaskTypeReturnedValue();
    }
    public void addTypeComplexOutput() {
        chooseType.setText(taskTypeComplexOutput.getText());
        mainInstance.setStageName("CEG - " + taskTypeComplexOutput.getText());
        addType(2);
        type = new TaskTypeComplexOutput();
    }
    public void addTypeGaps() {
        chooseType.setText(taskTypeGaps.getText());
        mainInstance.setStageName("CEG - " + taskTypeGaps.getText());
        addType(3);
        type = new TaskTypeGaps();
    }
    public void addTypeVarValue() {
        chooseType.setText(taskTypeVarValue.getText());
        mainInstance.setStageName("CEG - " + taskTypeVarValue.getText());
        addType(4);
        type = new TaskTypeVarValue();
    }
    public void addTypeLineNumbers() {
        chooseType.setText(taskTypeLineNumbers.getText());
        mainInstance.setStageName("CEG - " + taskTypeLineNumbers.getText());
        addType(5);
        type = new TaskTypeLineNumbers();
    }

    /**
     * Aktualizuje tekst polecenia.
     * @param text Lista linii zawierająca nową zawartość pola z poleceniem.
     */
    public void updateText(List<String> text) {
        this.text.clear();
        if(!text.isEmpty()) {
            int i = 0;
            String line;
            line = text.get(i);
            while (i<text.size()) {
                this.text.appendText(line + "\n");
                i++;
                if(i>=text.size()) break;
                line = text.get(i);
            }
        }
    }

    /**
     * Kończy tworzenie zadania w GUI. Tworzy nowy obiekt zadania, uzupełnia jego dane i zapisuje w egzaminie.
     * Dodaje nową zakładkę z zadaniem i przełącza sie na nią.
     * @param event
     * @throws Exception
     */
    public void finishEdition(ActionEvent event) throws Exception {
        Task t = new Task(type);
        t.setContents(contentList);
        t.getText().extractText(code);
        if (stage.getTitle().equals("Dodaj nowe zadanie")) {
            Exam.getInstance().addTask(t);
            mainInstance.getInstance().addNewTabPaneTab();
        }
        else if (stage.getTitle().equals("Edycja zadania")) {
            Exam.getInstance().editTask(Exam.getInstance().getCurrentTask());
            Exam.getInstance().setCurTask(t);
            if (!t.getContents().isEmpty()) {
                mainInstance.updateText(t.getContents());
            }
            mainInstance.updateCode(t.getText());
            mainInstance.showTask(true);
        }
        stage.hide();
        chooseType.setText("Typ zadania");
    }

    /**
     * Czyści pola znajdujące się w oknie dodawania zadania.
     */
    public static void clearFields() {
        manageTaskInstance.text.clear();
        manageTaskInstance.code.clear();
        manageTaskInstance.codeList = new ArrayList<>();
        manageTaskInstance.contentList = new ArrayList<>();
        manageTaskInstance.finish.setDisable(true);
    }

    /**
     * Przerywa edycję i zamyka okno dodawania zadania.
     * @param event
     * @throws Exception
     */
    public void cancelEdition(ActionEvent event) throws Exception {
        mainInstance.setStageName("CEG");
        stage.hide();
    }

    /**
     * Wyświetla okno wyboru pliku i ładuje go po zatwierdzeniu.
     */
    public void selectCodeFile() throws IOException {

        File file = fileChooser.showOpenDialog(stage);
        if(file != null) {
            fileChooser.setInitialDirectory(new File(file.getParent()));
            loadFile(file);
        }
    }

    /**
     * Laduje plik wybrany w oknie wyboru wpisując jego zawartość w polu CodeArea.
     * @param file
     * @throws IOException
     */
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