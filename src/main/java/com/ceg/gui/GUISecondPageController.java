package com.ceg.gui;


import java.util.*;
import javafx.scene.control.IndexRange;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
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
    List<String> list2;
    @FXML
    TextArea text;
    @FXML
    CodeArea code;
    @FXML
    TextArea result;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        //ustawienie numerowania linii dla części z kodem
        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        code.setWrapText(true);
        
        // utworzenie nowego egzaminu i dodanie pierwszego zadania
        Task t = new Task();
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
            for(String s : outcome) {
                result.appendText(s + "\n");
            }
        }

    }

    public void createPDF(ActionEvent actionEvent) throws IOException {
        PDFGenerator gen = new PDFGenerator("plik.pdf");
    }
/*
    public void compile(ActionEvent actionEvent) {
      
        //CodeParser.addNewlineAfterEachCoutFromFile(filename, resultFile);
        
         Exam inst = Exam.getInstance();
         List<String> result = new ArrayList<String>();
         inst.getCurrentTask().compiler.createFile(inst.getCurrentTask().getCode(), "zad1.cpp");
         inst.getCurrentTask().compiler.compile(result);

    }
*/
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
}