package com.ceg.gui;


import java.util.*;
import javafx.scene.control.IndexRange;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import com.ceg.examContent.TaskTypeMultipleOutput;
import com.ceg.examContent.TaskTypeSimpleOutput;
import com.ceg.pdf.PDFGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import java.io.IOException;
import java.net.URL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 *
 * @author Natalia
 */

public class GUISecondPageController implements Initializable {

    List<String> list;

    @FXML
    TextArea text;
    @FXML
    CodeArea code;
    @FXML
    ListView listView;

    final ObservableList<String> listItems = FXCollections.observableArrayList(" Typ1", " Typ2", " Typ3", " Typ4");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list = new ArrayList<>();
        listView.setItems(listItems);
        
        //ustawienie numerowania linii dla części z kodem
        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        code.setWrapText(true);
        
        // utworzenie nowego egzaminu i dodanie pierwszego zadania
        Task t = new Task();
        t.setType(new TaskTypeMultipleOutput());
        
        Exam.getInstance().init();
        Exam.getInstance().addTask(t);
        
        // pobranie z zadania (obiektu klasy Task) polecenia i kodu 
        List<String> txt = t.getContents();
        List<String> txt2 = t.getCode();
        // dodanie pobranego polecenia i kodu do TextArea
        int i = 0;
        String line;
        line = txt.get(i);
        while (i<txt.size()) {
            text.appendText(line + "\n");
            i++;
            if(i>=txt.size()) break;
            line = txt.get(i);
        }
        i=0;
        line = txt2.get(i);
        while (i<txt2.size()) {
            code.appendText(line + "\n");
            i++;
            if(i>=txt2.size()) break;
            line = txt2.get(i);
        }
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
                Exam.getInstance().getCurrentTask().setTestCode(new ArrayList<String>(Arrays.asList(newValue.split("\n"))));
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
                Exam.getInstance().getCurrentTask().setCode(new ArrayList<String>(Arrays.asList(newCode.split("\n")))); 
                Exam.getInstance().getCurrentTask().setPDFCode(new ArrayList<String>(Arrays.asList(newPDFCode.split("\n"))));
            }
        });
    }
    
    public void execute(ActionEvent actionEvent) {
       List<String> result = new ArrayList<>();
       List<String> answers = new ArrayList<>();
       
       Exam inst = Exam.getInstance();
       inst.getCurrentTask().getType().callExecute(inst.getCurrentTask(), result);
      // inst.getCurrentTask().compiler.execute(result);
        if(!result.isEmpty()){
                inst.getCurrentTask().getType().generateAnswers(result, answers);
                
                // TEMPORARY
                
                for(int i=0;i<answers.size();i++){
                    System.out.println(answers.get(i));
                }
                
                // TEMPORARY
        }
    }

    public void createPDF(ActionEvent actionEvent) throws IOException {
        String txt = new String();
        List<String> contentsList = Exam.getInstance().getCurrentTask().getContents();
        // pobranie treści polecenia w formie listy, sklejenie jej w jednego Stringa      
        for (int index = 0 ; index < contentsList.size(); index++) {
                String line = contentsList.get(index);
                txt+=line+"\n";
        }
        
        PDFGenerator gen = new PDFGenerator("plik.pdf", txt, Exam.getInstance().getCurrentTask().getPDFCode());
    }

    public void compile(ActionEvent actionEvent) {
      
        //CodeParser.addNewlineAfterEachCoutFromFile(filename, resultFile);
        
         Exam inst = Exam.getInstance();
         List<String> result = new ArrayList<>(); 
         inst.getCurrentTask().getType().callCompile(inst.getCurrentTask(), result);
         if(!result.isEmpty()){
             for(int i=0;i<result.size();i++){
                 System.out.println(result.get(i));
             }
         }
      //   inst.getCurrentTask().compiler.createFile(inst.getCurrentTask().getCode(), "zad1.cpp");
       //  inst.getCurrentTask().compiler.compile(result);

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
        code.setStyleClass(ir.getStart(), ir.getEnd(), className);
    }
}