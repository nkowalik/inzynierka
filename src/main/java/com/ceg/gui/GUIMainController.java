package com.ceg.gui;


import java.util.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.IndexRange;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import java.io.IOException;
import java.net.URL;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;


/**
 *
 * @author Natalia
 */
public class GUIMainController implements Initializable {

    @FXML
    TextArea text;
    @FXML
    CodeArea code;
    @FXML
    TabPane tabPane;
    @FXML
    TextArea result;
    @FXML
    Button executeBtn;
    @FXML
    Button testExecuteBtn;
    @FXML
    Button normalMarkerBtn;
    @FXML
    Button testMarkerBtn;
    @FXML
    Button hideMarkerBtn;
    @FXML
    Button gapsMarkerBtn;
    @FXML
    MenuItem changeAnswersNum;
    
    private static Stage stage = null;
    private static GUIMainController instance = null;
    private static Exam exam = null;
    private enum Status {
        ADD, DELETE, SWITCH
    }
    private Status status = Status.SWITCH;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        if(Exam.getInstance().idx == 0) {
            Exam.getInstance().init();
            exam = Exam.getInstance();
        }

        code.textProperty().addListener((observableValue, oldValue, newValue) -> {
            result.setText("");
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (status) {
                case DELETE:
                    if(Integer.parseInt(oldValue.getId()) == 0) { // usuwana jest pierwsza pozycja
                        updateTabPaneTabIndexes();
                        updateWindow(0);
                    }
                    else {
                        if(newValue != null) {
                            updateTabPaneTabIndexes();
                            updateWindow(Integer.parseInt(newValue.getId()));
                        }
                    }
                    status = Status.SWITCH;
                    break;
                case ADD:
                    status = Status.SWITCH;
                case SWITCH:
                    if(oldValue != null) {
                        int id = Integer.parseInt(oldValue.getId());
                        saveCode(id);
                        saveContent(id);
                        saveResult(id);
                    }
                    updateWindow(Integer.parseInt(newValue.getId()));
                    break;
            }
        });
        
        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        code.setWrapText(true);
        
        hideMarkerBtn.getStyleClass().add("hiddenButton");
        testMarkerBtn.getStyleClass().add("testButton");
        gapsMarkerBtn.getStyleClass().add("gapsButton");
        
        updateWindow(0);
    }
    public static synchronized void show() throws IOException {
        if(stage == null) {
            URL location = GUIMainController.class.getResource("/fxml/mainPage.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            
            Scene scene = new Scene((Pane)loader.load(location.openStream()));
            boolean result;          
            result = scene.getStylesheets().add("/styles/Styles.css");
            if(false == result){
                //TODO: report error
            }
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("CEG");
            stage.setResizable(false);
        }
        
        stage.show();
        stage.toFront();
    }
    public static GUIMainController getInstance() {
        return instance;
    }
    public void execute(ActionEvent actionEvent) {
        result.clear();
        saveCode(exam.idx);
        List<String> outcome = new ArrayList<String>();

        exam.getCurrentTask().getType().callExecute(exam.getCurrentTask(), outcome);
        for(String s : outcome) {
            result.appendText(s + "\n");
        }
        exam.getCurrentTask().setResult(result.getText());
    }
    public void createPDF(ActionEvent actionEvent) throws IOException {
        saveCode(exam.idx);
        saveContent(exam.idx);
        saveResult(exam.idx);
        PdfSavingController.show();
    }
    public void testMarker(ActionEvent actionEvent) {
        changeStyle("test");
    }    
    public void hideMarker(ActionEvent actionEvent) {
        changeStyle("hidden");
    }    
    public void normalMarker(ActionEvent actionEvent) {
        changeStyle("normal");
    }  
    public void gapsMarker(ActionEvent actionEvent) {
        changeStyle("gap");
    }  
    private void changeStyle(String className) {
        IndexRange ir = code.getSelection(); 
        int end = ir.getEnd();
        String c = code.getText();
        while (end < code.getLength()) {
            if (c.charAt(end) == '\n') {
                break;
            }
            end++;
        }
        code.setStyleClass(ir.getStart(), ir.getEnd(), className);
    }
    public void addTask(ActionEvent event) throws Exception {
        GUIAddTaskController.show();
    }
    public void deleteTask(ActionEvent event) throws Exception {
        if(Exam.getInstance().getTasks().isEmpty()) {
            showTask(false);
        }
        else {
            deleteCurrentTabPaneTab();
        }
    }
    public void changeNumberofAnswers(ActionEvent event) throws Exception {
        int answNum = Integer.MAX_VALUE-1;
        Dialog dialog;
        dialog = new TextInputDialog("MAX");
	dialog.setTitle("Liczba odpowiedzi");
	dialog.setHeaderText("Ile odpowiedzi będzie miało zadanie?");
	
	Optional<String> result = dialog.showAndWait();
	String entered = "MAX";
	 
	if (result.isPresent()) {	 
	    entered = result.get();
            if(!entered.contentEquals("MAX"))
                try{
                    answNum= Integer.valueOf(entered);
                }
                catch(NumberFormatException ex){
                    answNum=Integer.MAX_VALUE-1;
                }
        }
        exam.getTaskAtIndex(exam.idx).getType().getParams().setNoOfAnswers(answNum);
    }
    public void showTask(boolean visibility) {
        text.setVisible(visibility);
        code.setVisible(visibility);
        result.setVisible(visibility);
        executeBtn.setVisible(visibility);
        testExecuteBtn.setVisible(visibility);
        normalMarkerBtn.setVisible(visibility);
        testMarkerBtn.setVisible(visibility);
        hideMarkerBtn.setVisible(visibility);
       
        if(visibility){
            if(exam.getTaskAtIndex(exam.idx).getType().name.contentEquals("ComplexOutput")){
                changeAnswersNum.setVisible(visibility);
            }
            else{
                changeAnswersNum.setVisible(false);
            }
        }
        if(visibility){
            if(exam.getTaskAtIndex(exam.idx).getType().name.contentEquals("Gaps")){
                gapsMarkerBtn.setVisible(visibility);
            }
            else{
                gapsMarkerBtn.setVisible(false);
            }
        }
    }
    public void updateWindow(int idx) {
        if(exam.getTasks().isEmpty()) {  // gdy egzamin nie zawiera żadnych zadań
            showTask(false); // ukryj elementy związane z Taskiem
        }
        else {         
            Task t = exam.getTaskAtIndex(idx);
            exam.idx = idx;

            showTask(true);
            updateText(t.getContents());
            updateCode(t.getCode());
            updateResult(t.getResult());
        }
    }
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
    public void updateCode(List<String> text) {
        code.clear();
        if(!text.isEmpty()) {
            int i=0;
            String line = text.get(i);
            while (i<text.size()) {
                this.code.appendText(line + "\n");
                i++;
                if(i>=text.size()) break;
                line = text.get(i);
            }
        }
    }
    public void updateResult(String text) {
        this.result.clear();
        this.result.setText(text);
    }
    public static void setStageName (String str){
        stage.setTitle(str);
    }
    public void addNewTabPaneTab() {
        status = Status.ADD;
        Tab newTab = new Tab("Zadanie " + (exam.idx + 1));
        newTab.setId(Integer.toString(exam.idx));
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
    }
    public void deleteCurrentTabPaneTab() {
        status = Status.DELETE;
        exam.deleteTaskAtIndex(exam.idx);
        tabPane.getTabs().remove(exam.idx);
    }
    public void updateTabPaneTabIndexes() {
        for(int i = 0; i < tabPane.getTabs().size(); i++) {
            tabPane.getTabs().get(i).setId(Integer.toString(i));
            tabPane.getTabs().get(i).setText("Zadanie " + (i+1));
        }
    }
    public void saveCode(int idx) {
        Task task = Exam.getInstance().getTaskAtIndex(idx);
        task.setTestCode(new ArrayList<>((Arrays.asList(code.getText().split("\n")))));
        
            /* usuwa zamarkowane znaki i dodaje kod do klasy Task */
       
        String newCode = code.getText();
        String newPDFCode = code.getText();   
        
        for (int i = code.getText().length() - 1; i >= 0; i--) {
            List<String> s = (List<String>) code.getStyleOfChar(i);
            if (!s.isEmpty()) {
                if ("test".equals(s.get(s.size() - 1))) {
                    newCode = newCode.substring(0, i) + newCode.substring(i+1);
                    newPDFCode = newPDFCode.substring(0, i) + newPDFCode.substring(i+1);
                }
                if ("hidden".equals(s.get(s.size() - 1))) {
                    newPDFCode = newPDFCode.substring(0, i) + newPDFCode.substring(i+1);
                }
                if (task.getType().name == "Gaps" && "gap".equals(s.get(s.size() - 1))) {
                    newPDFCode = newPDFCode.substring(0, i) + "_" + newPDFCode.substring(i+1);
                }
            }
        }
        task.setCode(new ArrayList<String>(Arrays.asList(newCode.split("\n"))));
        task.setPDFCode(new ArrayList<String>(Arrays.asList(newPDFCode.split("\n"))));
        
    }
    public void saveContent(int idx) {
        Exam.getInstance().getTaskAtIndex(idx).setContents(Arrays.asList(text.getText().split("\n")));
    }
    public void saveResult(int idx) {
        Exam.getInstance().getTaskAtIndex(idx).setResult(result.getText());
    }
}