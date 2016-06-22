package com.ceg.gui;

import com.ceg.examContent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Natalia
 */


public class GUIAddTaskController {
    ArrayList<String> contentList = new ArrayList<>();
    @FXML
    TextArea text;
    @FXML
    TextArea code;

    public void addType1() {
        try {
            Scanner s = new Scanner(new File("polecenie1.txt"));
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

        // TO DO
    }
    public void addType2() {
        try {
            Scanner s = new Scanner(new File("polecenie2.txt"));
            contentList.clear();
            while (s.hasNext()) {
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

        // TO DO
    }

    public void finishEdition(ActionEvent event) throws Exception {
        Task t = new Task();
        t.setContents(contentList); //////////////////////////////// Powinno modyfikować zawartość SecondPage ?
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
    }

    public void cancelEdition(ActionEvent event) throws Exception {
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
    }
}
