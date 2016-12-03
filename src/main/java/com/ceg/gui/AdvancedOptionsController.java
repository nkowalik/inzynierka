package com.ceg.gui;

import com.ceg.examContent.Exam;
import com.ceg.pdf.PDFSettings;
import com.ceg.utils.ColorPicker;
import com.ceg.utils.ColorPickerUtil;
import com.ceg.utils.FontTypeUtil;
import com.ceg.xml.TaskData;
import com.ceg.xml.TasksLoading;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    ChoiceBox answerFont;
    @FXML
    TextField answerFontSize;
    @FXML
    ChoiceBox fontColor;
    @FXML
    CheckBox isBold;
    @FXML
    CheckBox isItalic;
    @FXML
    Slider changeTimeout;
    @FXML
    CheckBox separators;
    @FXML
    Menu chooseType;
    @FXML
    TextArea text;
    @FXML
    TextField examTitle;
    @FXML
    TextField examComment;
    
    public static Stage appStage;
    private PDFSettings pdfSettings;
    private TaskData taskData;
    private List<String> taskNames = new ArrayList(Arrays.asList("Simple output", "Returned value",
            "Complex output", "Gaps", "Var value", "Line numbers", "Own type"));
    private int activeTask = -1;
    private final List<String> fontList = FontTypeUtil.getFontNamesList();
    private List<String> colorList;

    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        colorList = Arrays.asList(ColorPicker.values())
                .stream()
                .map(p -> p.getColorName())
                .collect(Collectors.toList());
        pdfSettings = PDFSettings.getInstance();
        
        commandFont.setItems(FXCollections.observableList(fontList));
        commandFont.setValue(pdfSettings.getCommandFont().getFontName());
        
        codeFont.setItems(FXCollections.observableList(fontList));
        codeFont.setValue(pdfSettings.getCodeFont().getFontName());
        
        answerFont.setItems(FXCollections.observableList(fontList));
        answerFont.setValue(pdfSettings.getAnswerFont().getFontName());
        
        fontColor.setItems(FXCollections.observableList(colorList));
        fontColor.setValue(ColorPicker.BLACK.getColorName());
        
        commandFontSize.setText(pdfSettings.getCommandFontSize().toString());
        codeFontSize.setText(pdfSettings.getCodeFontSize().toString());
        answerFontSize.setText(pdfSettings.getAnswerFontSize().toString());
        
        isBold.setSelected(pdfSettings.getIsAnswerBold());
        isItalic.setSelected(pdfSettings.getIsAnswerItalic());
        
        changeTimeout.valueProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue<? extends Number> ov,
                Number oldVal, Number newVal){
                Exam.getInstance().setExecutionTimetout(newVal.floatValue());
            }
        });
        
        separators.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, 
                Boolean oldVal, Boolean newVal) -> {
            PDFSettings.getInstance().setSeparatorsAfterTasks(newVal);
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
        
        PDFSettings.getInstance().setAnswerFont(FontTypeUtil.change(answerFont.getValue().toString()));
       
        PDFSettings.getInstance().setCommandFontSize(Integer.parseInt(commandFontSize.getText()));
        PDFSettings.getInstance().setCodeFontSize(Integer.parseInt(codeFontSize.getText()));
        PDFSettings.getInstance().setAnswerFontSize(Integer.parseInt(answerFontSize.getText()));
        
        PDFSettings.getInstance().setAnswerColor(ColorPickerUtil.change(fontColor.getValue().toString()));
        PDFSettings.getInstance().setIsAnswerBold(isBold.isSelected());
        PDFSettings.getInstance().setIsAnswerItalic(isItalic.isSelected());
        
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

    public void saveOptions(ActionEvent event) {
        if (examTitle != null) {
            Exam.title = examTitle.getText();
        }
        if (examComment != null) {
            Exam.comment = examComment.getText();
        }
    }
}
