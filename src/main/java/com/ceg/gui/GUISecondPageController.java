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

        Task t = new Task();
        Exam.getInstance().init();
        Exam.getInstance().addTask(t);
        ArrayList<String> txt = t.getContents();
        ArrayList<String> txt2 = t.getCode();
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
        String text = new String();
        ArrayList<String> contentsList = Exam.getInstance().getCurrentTask().getContents();
                
        for (int index = 0 ; index < contentsList.size(); index++) {
                String line = contentsList.get(index);
                text+=line+"\n";
        }
        PDFGenerator gen = new PDFGenerator("plik.pdf", text, Exam.getInstance().getCurrentTask().getCode());
    }

    public void compile(ActionEvent actionEvent) {
      
        //CodeParser.addNewlineAfterEachCoutFromFile(filename, resultFile);
        
         Exam inst = Exam.getInstance();
         List<String> result = new ArrayList<String>();
         inst.getCurrentTask().compiler.createFile(inst.getCurrentTask().getCode(), "zad1.cpp");
         inst.getCurrentTask().compiler.compile(result);

    }

}
