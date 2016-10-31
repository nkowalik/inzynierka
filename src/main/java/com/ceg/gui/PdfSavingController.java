package com.ceg.gui;

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
import java.util.logging.Level;
import javafx.beans.value.ChangeListener;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Klasa reprezentująca kontroler okna do generowania pliku .pdf.
 */
public class PdfSavingController implements Initializable {

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

    private final List<String> testTypeList = Arrays.asList("student", "nauczyciel");
    private final List<String> monthList = new ArrayList<>();
    private final List<String> yearList = new ArrayList<>();
    
    private final ObservableList<String> daysList = FXCollections.observableList(new ArrayList<String>());
    
    public static Stage appStage;
    private PDFSettings pdfSettings;

    /**
     * Dokonuje inicjalizacji głównego okna generacji pliku .pdf.
     * Ustawia domyślne wartości, listenery.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        pdfSettings = PDFSettings.getInstance();
        
        for (Integer i = 1; i <= 12; i++) {
            if (i < 10) {
                monthList.add('0' + i.toString());
            }
            else {
                monthList.add(i.toString());
            }
        }
        
        testType.setItems(FXCollections.observableList(testTypeList));
        testType.setValue(pdfSettings.getTestType());
        
        dateMonth.setItems(FXCollections.observableList(monthList));
        Integer month = pdfSettings.getMonth();
        if (month < 10)
            dateMonth.setValue('0' + month.toString());
        else
            dateMonth.setValue(month.toString());
        
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

        // todo zamienić listenery na zapis przy nacisnięciu przycisku 'zapisz' (aktualizacja ustawień przy każdej zmianie jest zbyteczna)
        dateMonth.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                daysList.clear();
                PDFSettings.getInstance().setMonth(newValue.intValue() + 1);
                YearMonth yM = YearMonth.of(PDFSettings.getInstance().getYear(), PDFSettings.getInstance().getMonth());
                Integer days = yM.lengthOfMonth();
                for (Integer i = 1; i <= days; i++) {
                    daysList.add(i.toString());
                }
                dateDay.setValue(PDFSettings.getInstance().getDay().toString());
            }
        });
        
        dateDay.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() != -1)
                    PDFSettings.getInstance().setDay(Integer.parseInt(daysList.get(newValue.intValue())));
            }           
        });
        
        dateYear.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                PDFSettings.getInstance().setYear(Integer.parseInt(yearList.get(newValue.intValue())));
                
                daysList.clear();
                
                YearMonth yM = YearMonth.of(PDFSettings.getInstance().getYear(), PDFSettings.getInstance().getMonth());
                Integer days = yM.lengthOfMonth();
                for (Integer i = 1; i <= days; i++) {
                    daysList.add(i.toString());
                }
                dateDay.setValue(PDFSettings.getInstance().getDay().toString());
            }           
        });
    }

    /**
     * Wyświetla okno generowania pliku .pdf.
     * @throws IOException
     */
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

    /**
     * Zapisuje egzamin w formie pliku .pdf.
     * W przypadku istnienia pliku o podanej nazwie wyświetla komunikat z odpowiednią informacją.
     * @param event
     * @throws IOException
     */
    public void saveFile(ActionEvent event) throws IOException {   
        
        PDFSettings.getInstance().setTestType(testType.getValue().toString());
        PDFSettings.getInstance().setPdfFilePath(filePath.getText());
        String extension = fileName.getText().substring(fileName.getText().length() - 4, fileName.getText().length());
        if (!extension.equals(".pdf")) {
            PDFSettings.getInstance().setPdfFileName(fileName.getText() + ".pdf");
        }
        else {
            PDFSettings.getInstance().setPdfFileName(fileName.getText());
        }
        PDFSettings.getInstance().saveFile();
        File pdfFile = PDFSettings.getInstance().getPdfFile();

        if (pdfFile.exists() && !pdfFile.isDirectory()) {
            PdfOverwritingController.show();
        }

        else {
            GUIExamCompilationController.show();
        }
    }

    /**
     * Kończy proces generacji pliku .pdf (przed wciśnięciem przycisku 'Zapisz plik').
     * @param event
     */
    public void cancel(ActionEvent event) {
        appStage.hide();
    }

    /**
     * Otwiera okno wyboru lokalizacji zapisu pliku.
     * @param event
     */
    public void browse(ActionEvent event) {        
        DirectoryChooser dirChooser = new DirectoryChooser () ;
        dirChooser.setTitle("Wybierz lokalizację pliku");
        File dir = dirChooser.showDialog(appStage);
        
        if (dir != null) {
            filePath.setText(dir.getAbsolutePath());
        }
    }

    /**
     * Otwiera okno ustawień zaawansowanych.
     * @param event
     */
    public void advancedOptions(ActionEvent event){
        try {
            AdvancedOptionsController.show();
        } catch (IOException ex) {
            Logger.getLogger(PdfSavingController.class.getName()).log(Level.SEVERE, null, ex); // TODO: obsluga wyjatku
        }
    }
}