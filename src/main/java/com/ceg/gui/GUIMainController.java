package com.ceg.gui;


import java.util.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.IndexRange;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import com.ceg.pdf.PDFGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import java.io.IOException;
import java.net.URL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
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
    TextArea result;
    @FXML
    Button executeBtn;
    @FXML
    Button testExecuteBtn;
    
    private static Stage stage = null;
    private static GUIMainController instance = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        if(Exam.getInstance().idx == 0)
            Exam.getInstance().init();
        // dodanie listenerów na zmiany w polach tekstowych
        text.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                Exam.getInstance().getCurrentTask().setContents(Arrays.asList(newValue.split("\n")));
                
            }
        });
        code.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {                
                Exam.getInstance().getCurrentTask().setTestCode(Arrays.asList(newValue.split("\n")));
                /* usuwa zamarkowane znaki i dodaje kod do klasy Task */
                String newCode = newValue;
                String newPDFCode = newValue;
                
                for (int i = newValue.length() - 1; i >= 0; i--) {
                    List<String> s = (List<String>) code.getStyleOfChar(i);
                    if (!s.isEmpty()) {
                        if ("test".equals(s.get(s.size() - 1))) {
                            newCode = newCode.substring(0, i) + newCode.substring(i+1);
                            newPDFCode = newPDFCode.substring(0, i) + newPDFCode.substring(i+1);
                        }
                        if ("hidden".equals(s.get(s.size() - 1))) {
                            newPDFCode = newPDFCode.substring(0, i) + newPDFCode.substring(i+1);
                        }
                    }
                }
                Exam.getInstance().getCurrentTask().setCode(Arrays.asList(newCode.split("\n"))); 
                Exam.getInstance().getCurrentTask().setPDFCode(Arrays.asList(newPDFCode.split("\n")));
            }
        });
        
        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        code.setWrapText(true);
        
        updateWindow();
    }
    public static synchronized void show() throws IOException {
        if(stage == null) {
            URL location = GUIMainController.class.getResource("/fxml/mainPage.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            
            Scene scene = new Scene((Pane)loader.load(location.openStream()));
            
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("CEG");
        }
        
        stage.show();
        stage.toFront();
    }
    public static GUIMainController getInstance() {
        return instance;
    }
    public void execute(ActionEvent actionEvent) {
        List<String> outcome = new ArrayList<String>();

        Exam inst = Exam.getInstance();

        inst.getCurrentTask().compiler.createFile(inst.getCurrentTask().getCode(), "zad1.cpp");
        inst.getCurrentTask().compiler.compile(outcome);

        /* uwaga, ten warunek moze nie dzialac na kompilatorze linuxa - jesli nie dziala, trzeba go bedzie zmienic */
        if(outcome.isEmpty()) // jesli kompilacja przebiegla pomyslnie
            inst.getCurrentTask().compiler.execute(outcome);

        if(!outcome.isEmpty()){
            result.clear();
            //result.appendText("");
            for(String s : outcome) {
                result.appendText(s + "\n");
            }
        }

    }
    public void createPDF(ActionEvent actionEvent) throws IOException {
        Stage pdfSavingStage = new Stage();
        Parent scene = FXMLLoader.load(getClass().getResource("/fxml/pdfSaving.fxml"));
        pdfSavingStage.setTitle("Zapisz plik");
        pdfSavingStage.setScene(new Scene(scene, 600, 400));
        pdfSavingStage.show();
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
        
        if(Exam.getInstance().getTasks().size() == 0) {
            text.setVisible(false);
            code.setVisible(false);
            result.setVisible(false);
            executeBtn.setVisible(false);
            testExecuteBtn.setVisible(false);
        }
        else {
            Exam.getInstance().deleteCurrentTask();
            updateWindow();
        }
    }
    public void showTask(boolean visibility) {
        text.setVisible(visibility);
        code.setVisible(visibility);
        result.setVisible(visibility);
        executeBtn.setVisible(visibility);
        testExecuteBtn.setVisible(visibility);
    }
    public void updateWindow() {
        if(Exam.getInstance().getTasks().isEmpty()) {  // gdy egzamin nie zawiera żadnych zadań
            showTask(false); // ukryj elementy związane z Taskiem
        }
        else {
            showTask(true);
            Task t = Exam.getInstance().getCurrentTask();
            updateText(t.getContents()); // może Text, żeby pasowało do konwencji nazw
            updateCode(t.getCode());
            clearResult();
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
    public void clearResult() {
        result.clear();
    }
}