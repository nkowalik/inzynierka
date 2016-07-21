package com.ceg.gui;

import com.ceg.examContent.Exam;
import com.ceg.pdf.PDFSettings;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Martyna
 */
public class PdfSavingController implements Initializable {
    @FXML
    ChoiceBox commandFont;
    @FXML
    TextField commandFontSize;

    @FXML
    ChoiceBox codeFont;
    @FXML
    TextField codeFontSize;

    @FXML
    ChoiceBox testType;
    @FXML
    TextField filePath;
    @FXML
    ChoiceBox dateDay;
    @FXML
    ChoiceBox dateMonth;
    @FXML
    ChoiceBox dateYear;
    @FXML
    TextField fileName;

    private final List<String> fontList = Arrays.asList("Arial", "Courier","Times New Roman", "Verdana");
    //private final List<String> testTypeList = Arrays.asList("student", "nauczyciel", "interaktywny");
    private final List<String> testTypeList = Arrays.asList("student", "nauczyciel");
    private final List<String> monthList = new ArrayList<>();
    private final List<String> yearList = new ArrayList<>();

    private final ObservableList<String> daysList = FXCollections.observableList(new ArrayList<String>());

    public static Stage appStage;
    private PDFSettings pdfSettings;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pdfSettings = Exam.getInstance().pdfSettings;

        Calendar calendar = Calendar.getInstance();

        for (Integer i = 1; i <= 12; i++) {
            if (i < 10) {
                monthList.add('0' + i.toString());
            }
            else {
                monthList.add(i.toString());
            }
        }
        commandFont.setItems(FXCollections.observableList(fontList));
        commandFont.setValue(pdfSettings.getCommandFont());

        codeFont.setItems(FXCollections.observableList(fontList));
        codeFont.setValue(pdfSettings.getCodeFont());

        testType.setItems(FXCollections.observableList(testTypeList));
        testType.setValue(pdfSettings.getTestType());

        dateMonth.setItems(FXCollections.observableList(monthList));
        Integer month = pdfSettings.getMonth();
        if (month < 10)
            dateMonth.setValue('0' + month.toString());
        else
            dateMonth.setValue(month.toString());

        commandFontSize.setText(pdfSettings.getCommandFontSize().toString());
        codeFontSize.setText(pdfSettings.getCodeFontSize().toString());

        Integer year = pdfSettings.getYear();
        for (Integer i  = 2008; i <= year+10; i++) {
            yearList.add(i.toString());
        }
        dateYear.setItems(FXCollections.observableList(yearList));
        dateYear.setValue(year.toString());

        YearMonth yM = YearMonth.of(year, month);
        Integer days = yM.lengthOfMonth();
        for (Integer i = 1; i <= days; i++) {
            daysList.add(i.toString());
        }
        dateDay.setItems(daysList);
        dateDay.setValue(pdfSettings.getDay().toString());

        filePath.setText(pdfSettings.getPdfFilePath());
        fileName.setText(pdfSettings.getPdfFileName());

        dateMonth.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                daysList.clear();
                pdfSettings.setMonth(newValue.intValue() + 1);
                YearMonth yM = YearMonth.of(pdfSettings.getYear(), pdfSettings.getMonth());
                Integer days = yM.lengthOfMonth();
                for (Integer i = 1; i <= days; i++) {
                    daysList.add(i.toString());
                }
                dateDay.setValue(pdfSettings.getDay().toString());
            }
        });

        dateDay.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() != -1)
                    pdfSettings.setDay(Integer.parseInt(daysList.get(newValue.intValue())));
            }
        });

        dateYear.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pdfSettings.setYear(Integer.parseInt(yearList.get(newValue.intValue())));

                daysList.clear();

                YearMonth yM = YearMonth.of(pdfSettings.getYear(), pdfSettings.getMonth());
                Integer days = yM.lengthOfMonth();
                for (Integer i = 1; i <= days; i++) {
                    daysList.add(i.toString());
                }
                dateDay.setValue(pdfSettings.getDay().toString());
            }
        });
    }

    public static synchronized void show() throws IOException {
        if(appStage == null) {
            URL location = GUIMainController.class.getResource("/fxml/pdfSaving.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);

            Scene scene = new Scene((Pane)loader.load(location.openStream()));
            appStage = new Stage();
            appStage.setScene(scene);
            appStage.setTitle("Zapisz plik");
            appStage.setResizable(false);
        }

        appStage.show();
        appStage.toFront();
    }

    public void saveFile(ActionEvent event) throws IOException {
        //appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        pdfSettings.setCommandFont(commandFont.getValue().toString());
        pdfSettings.setCodeFont(codeFont.getValue().toString());

        pdfSettings.setCommandFontSize(Integer.parseInt(commandFontSize.getText()));
        pdfSettings.setCodeFontSize(Integer.parseInt(codeFontSize.getText()));

        pdfSettings.setTestType(testType.getValue().toString());
        pdfSettings.setPdfFilePath(filePath.getText());
        String extension = fileName.getText().substring(fileName.getText().length() - 4, fileName.getText().length());
        if (!extension.equals(".pdf")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Brak implementacji");
            alert.setHeaderText("Nazwa pliku nie kończy się rozszerzeniem .pdf");

            alert.showAndWait();
        }
        else {
            pdfSettings.setPdfFileName(fileName.getText());

            pdfSettings.saveFile();
            File pdfFile = pdfSettings.getPdfFile();
            Exam.getInstance().pdfSettings = pdfSettings;

            if (pdfFile.exists() && !pdfFile.isDirectory()) {
                Stage ifPdfExistStage = new Stage();
                Parent scene = FXMLLoader.load(getClass().getResource("/fxml/ifPdfExist.fxml"));
                ifPdfExistStage.setTitle("Czy chcesz nadpisać?");
                ifPdfExistStage.setScene(new Scene(scene, 430, 125));
                ifPdfExistStage.initStyle(StageStyle.TRANSPARENT);
                ifPdfExistStage.setResizable(false);
                ifPdfExistStage.setAlwaysOnTop(true);
                ifPdfExistStage.show();
            }

            else {
                Exam.getInstance().pdfSettings.pdfGenerate(appStage);
                appStage.hide();
            }
        }
    }

    public void cancel(ActionEvent event) {
        //appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
    }

    public void browse(ActionEvent event) {
        //appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        DirectoryChooser dirChooser = new DirectoryChooser () ;
        dirChooser.setTitle("Wybierz lokalizację pliku");
        File dir = dirChooser.showDialog(appStage);

        if (dir != null) {
            filePath.setText(dir.getAbsolutePath());
        }
    }
}
