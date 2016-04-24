package com.ceg.gui;


import com.ceg.compiler.Ccompiler;
import com.ceg.compiler.CodeParser;
import com.ceg.pdf.PDFGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import org.apache.commons.lang.SystemUtils;


/**
 *
 * @author Natalia
 */

public class GUISecondPageController implements Initializable {

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
                list.add(s1.nextLine());
                code.appendText(list.get(i) + "\n");
            }

            s1.close();

        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }

    public void execute(ActionEvent actionEvent) {
        List<String> result = new ArrayList<String>();
        if(SystemUtils.IS_OS_LINUX)
            Ccompiler.runAndReadOutputOnLinux("executable", result, 10);
        else if(SystemUtils.IS_OS_WINDOWS)
            Ccompiler.runAndReadOutput("wynik.exe", result, 10);
        if(!result.isEmpty()){
                // do something useful here
        }
    }

    public void createPDF(ActionEvent actionEvent) throws IOException {
        PDFGenerator gen = new PDFGenerator("plik.pdf", "Tekst polecenia", list);
    }

    public void compile(ActionEvent actionEvent) {
        Ccompiler compiler = new Ccompiler(); 
       // String filename = "test.cpp";
        String resultFile = "wynik.cpp";
        //CodeParser.addNewlineAfterEachCoutFromFile(filename, resultFile);
        if(SystemUtils.IS_OS_LINUX)
            compiler.CompileNRunOnLinux(resultFile); // put .cpp file in program directory! (TODO: allow random location)
        else if(SystemUtils.IS_OS_WINDOWS)
            compiler.CompileNRun(resultFile);
    }

}
