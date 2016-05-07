package com.ceg.gui;


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
import java.util.*;
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
    TextArea code;
    @FXML
    ListView listView;

    final ObservableList<String> listItems = FXCollections.observableArrayList(" Typ1", " Typ2", " Typ3", " Typ4");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        listView.setItems(listItems);
        
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
                Exam.getInstance().getCurrentTask().setCode(Arrays.asList(newValue.split("\n")));
            }
        });
    }

    public void execute(ActionEvent actionEvent) {
       List<String> result = new ArrayList<String>();
       
       Exam inst = Exam.getInstance();
       inst.getCurrentTask().compiler.execute(result);
        if(!result.isEmpty()){
                // do something useful here
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
        PDFGenerator gen = new PDFGenerator("plik.pdf", txt, Exam.getInstance().getCurrentTask().getCode());
    }

    public void compile(ActionEvent actionEvent) {
      
        //CodeParser.addNewlineAfterEachCoutFromFile(filename, resultFile);
        
         Exam inst = Exam.getInstance();
         List<String> result = new ArrayList<String>();
         inst.getCurrentTask().compiler.createFile(inst.getCurrentTask().getCode(), "zad1.cpp");
         inst.getCurrentTask().compiler.compile(result);

    }

}
