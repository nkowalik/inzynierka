package com.ceg.gui;

import com.ceg.examContent.Exam;
import com.ceg.pdf.PDFSettings;
import com.ceg.utils.FontTypeUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.ceg.xml.TaskData;
import com.ceg.xml.TasksLoading;
import com.google.common.collect.ArrayTable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Klasa reprezentująca kontroler opcji zaawansowanych.
 */
public class AdvancedOptionsController implements Initializable {

    @FXML
    ChoiceBox commandFont;
    @FXML
    TextField commandFontSize;
    @FXML
    ChoiceBox codeFont;
    @FXML
    TextField codeFontSize;
    @FXML
    Slider changeTimeout;
    @FXML
    Menu chooseType;
    @FXML
    TextArea text;
    @FXML
    Button confirm;
    
    public static Stage appStage;
    private PDFSettings pdfSettings;
    private TaskData taskData;
    private List<String> taskNames = new ArrayList(Arrays.asList("Simple output", "Returned value",
            "Complex output", "Gaps", "Var value", "Line numbers", "Own type"));
    private int activeTask = -1;
    private final List<String> fontList = FontTypeUtil.getFontNamesList();

    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        pdfSettings = PDFSettings.getInstance();
        
        commandFont.setItems(FXCollections.observableList(fontList));
        commandFont.setValue(pdfSettings.getCommandFont().getFontName());
        
        codeFont.setItems(FXCollections.observableList(fontList));
        codeFont.setValue(pdfSettings.getCodeFont().getFontName());
        
        commandFontSize.setText(pdfSettings.getCommandFontSize().toString());
        codeFontSize.setText(pdfSettings.getCodeFontSize().toString());
        
        changeTimeout.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov,
                Number oldVal, Number newVal){
                Exam.getInstance().setExecutionTimetout(newVal.floatValue());
            }
        });

        taskData = TasksLoading.loadFromXml();

        for(MenuItem item : chooseType.getItems()) {
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    chooseType.setText(item.getText());
                    activeTask = chooseType.getItems().indexOf(item);
                    for(TaskData td : taskData.getTaskData()) {
                        if(td.getName().equals(taskNames.get(activeTask))) {
                            text.setText(td.getText());
                            break;
                        }
                    }
                }
            });
        }
    }

    /**
     * Wyświetla okno z ustawieniami zaawansowanymi, którego wygląd określony jest w odpowiednim pliku .fxml.
     * @throws IOException
     */
    public static synchronized void show() throws IOException {
        if(appStage == null) {
            URL location = GUIMainController.class.getResource("/fxml/advancedOptions.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            
            Scene scene = new Scene((Pane)loader.load(location.openStream()));
            appStage = new Stage();
            appStage.setScene(scene);
            appStage.setTitle("Opcje zaawansowane");
            appStage.setResizable(false);
        }
        
        appStage.show();
        appStage.toFront();
    }

    /**
     * Zmienia ustawienia generowania pliku .pdf (czcionka i jej rozmiar) na te, które zostały wybrane w oknie.
     * @param event
     * @throws IOException
     */
    public void apply(ActionEvent event) throws IOException {   
        PDFSettings.getInstance().setCommandFont(FontTypeUtil.change(commandFont.getValue().toString()));
        PDFSettings.getInstance().setCodeFont(FontTypeUtil.change(codeFont.getValue().toString()));
        
        PDFSettings.getInstance().setCommandFontSize(Integer.parseInt(commandFontSize.getText()));
        PDFSettings.getInstance().setCodeFontSize(Integer.parseInt(codeFontSize.getText()));
        
        appStage.hide();
    }

    /**
     * Zamyka okno opcji zaawansowanych.
     * @param event
     */
    public void cancel(ActionEvent event) {
        appStage.hide();
    }

    public void saveTask(ActionEvent event) throws  IOException {
        if(activeTask != -1) {
            taskData.getTaskData().get(activeTask).setText(text.getText());
            TasksLoading.saveToXml(taskData);
        }
    }
}
