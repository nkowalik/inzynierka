package com.ceg.gui;

import com.ceg.examContent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Natalia
 */


public class GUIAddTaskController extends Component {
    ArrayList<String> contentList = new ArrayList<>();
    ArrayList<String> codeList = new ArrayList<>();
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

        // TODO
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

        // TODO
    }

    public void finishEdition(ActionEvent event) throws Exception {
        Task t = new Task();
        t.setContents(contentList); // TODO: Powinno modyfikować zawartość SecondPage
        t.setCode(codeList);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
    }

    public void cancelEdition(ActionEvent event) throws Exception {
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
    }

    public void selectCodeFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                Scanner s = new Scanner(new File(selectedFile.getName()));
                System.out.println(selectedFile.getName());
                codeList.clear();
                while(s.hasNext()) {
                    codeList.add(s.nextLine());
                }
                s.close();
                code.clear();
                for (String i : codeList) {
                    code.appendText(i + "\n");
                }
            } catch (FileNotFoundException ex) {
                System.err.println(ex);
            }
        }

    }
}
