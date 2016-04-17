package com.ceg.gui;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class SecondPageController implements Initializable {

    List<String> list;

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
        listView.setItems(listItems);

        try {
            Scanner s = new Scanner(new File("polecenie.txt"));
            while (s.hasNext()) {
                text.appendText(s.nextLine() + "\n");
            }
            s.close();

            Scanner s1 = new Scanner(new File("kod.txt"));
            for (int i = 0; s1.hasNext(); i++) {
                list.add(s1.nextLine() + "\n");
                code.appendText(list.get(i));
            }
            s1.close();

        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }

    public void execute(ActionEvent actionEvent) {
    }

    public void createPDF(ActionEvent actionEvent) {
        
    }

    public void compile(ActionEvent actionEvent) {
    }

}
