package com.ceg.gui;

import com.ceg.examContent.Content;
import com.ceg.utils.Alerts;
import java.util.*;
import com.ceg.examContent.Text;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;

import javafx.scene.layout.Pane;
import com.ceg.exceptions.EmptyExamException;
import static com.ceg.utils.ContentCssClass.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.WindowEvent;
import org.fxmisc.richtext.StyleClassedTextArea;

/**
 * Klasa reprezentująca kontroler głównego okna programu.
 */
public class GUIMainController implements Initializable {

    @FXML
    MenuBar menu;
    @FXML
    StyleClassedTextArea text;
    @FXML
    CodeArea code;
    @FXML
    TabPane tabPane;
    @FXML
    TextArea result;
    @FXML
    Button executeBtn;
    @FXML
    Button testExecuteBtn;
    @FXML
    Button normalMarkerBtn;
    @FXML
    Button testMarkerBtn;
    @FXML
    Button hideMarkerBtn;
    @FXML
    Button gapsMarkerBtn;
    @FXML
    MenuItem changeAnswersNum;
    @FXML
    MenuItem changeNameItem;
    @FXML
    HBox textOptions;
    @FXML
    private void advancedOptionsClicked(MouseEvent event){
        try {
            AdvancedOptionsController.show();
        } catch (IOException ex) {
            Logger.getLogger(PdfSavingController.class.getName()).log(Level.SEVERE, null, ex); // TODO: obsluga wyjatku
        }
    }
     
    private static Stage stage = null;
    private static GUIMainController instance = null;
    private static Exam exam = null;
    public enum Status {
        ADD, DELETE, SWITCH, RENAME, DRAG
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private Status status = Status.SWITCH;

    public static void setStageName (String str){
        stage.setTitle(str);
    }

    /**
     * Dokonuje inicjalizacji okna głównego, ustawia listenery na zmianę kodu oraz przełączanie zakładek.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        if(Exam.getInstance().idx == 0) {
            Exam.getInstance().init();
            exam = Exam.getInstance();
        }

        code.textProperty().addListener((observableValue, oldValue, newValue) -> {
            result.setText("");
        });

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (status) {
                case RENAME:
                case DRAG:
                    break;
                case DELETE:
                    if(Integer.parseInt(oldValue.getId()) == 0) { // usuwana jest pierwsza pozycja
                        updateTabPaneTabIndexes();
                        updateWindow(0);
                    }
                    else {
                        if(newValue != null) {
                            updateTabPaneTabIndexes();
                            updateWindow(Integer.parseInt(newValue.getId()));
                        }
                    }
                    status = Status.SWITCH;
                    break;
                case ADD:
                    status = Status.SWITCH;
                case SWITCH:
                    if(oldValue != null) {
                        int id = Integer.parseInt(oldValue.getId());
                        saveText(id);
                        saveContent(id);
                        saveResult(id);
                    }
                    updateWindow(Integer.parseInt(newValue.getId()));
                    break;
            }
        });

        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        code.setWrapText(true);
        
        hideMarkerBtn.getStyleClass().add("hiddenButton");
        testMarkerBtn.getStyleClass().add("testButton");
        gapsMarkerBtn.getStyleClass().add("gapsButton");
        
        updateWindow(0);
    }

    /**
     * Wyświetla główne okno programu.
     * @throws IOException
     */
    public static synchronized void show() throws IOException {
        if(stage == null) {
            URL location = GUIMainController.class.getResource("/fxml/mainPage.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            
            Scene scene = new Scene((Pane)loader.load(location.openStream()));
            boolean result;          
            result = scene.getStylesheets().add("/styles/Styles.css");
            if(false == result){
                //TODO: report error
            }
            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("CEG");
            stage.setResizable(false);
        }
        
        stage.show();
        stage.toFront();
        stage.setOnCloseRequest(e -> Platform.exit());
    }
    public static GUIMainController getInstance() {
        return instance;
    }

    /**
     * Wykonuje kod zawarty w polu CodeArea. Rezultat zapisuje w odpowiednim obiekcie klasy Task i wyświetla w oknie programu.
     * @param actionEvent
     */
    public void execute(ActionEvent actionEvent) {
        result.clear();
        saveText(exam.idx);
        List<String> outcome = new ArrayList<>();

        exam.getCurrentTask().getType().callExecute(exam.getCurrentTask(), outcome);
        for(String s : outcome) {
            result.appendText(s + "\n");
        }
        exam.getCurrentTask().setResult(result.getText());
    }

    public void testExecute(ActionEvent actionEvent) {
        result.clear();
        saveText(exam.idx);
        List<String> outcome = new ArrayList<>();

        exam.getCurrentTask().getType().callTestExecute(exam.getCurrentTask(), outcome);
        for(String s : outcome) {
            result.appendText(s + "\n");
        }
        exam.getCurrentTask().setResult(result.getText());

    }

    /**
     * Otwiera okno generowania pliku .pdf na podstawie zadań zawartych w egzaminie.
     * @param actionEvent
     * @throws IOException
     */
    public void createPDF(ActionEvent actionEvent) throws IOException {
            try {
                if (exam.getTasks().isEmpty()) {
                    throw new EmptyExamException();
                }
                saveText(exam.idx);
                saveContent(exam.idx);
                saveResult(exam.idx);
                PdfSavingController.show();
            } catch (EmptyExamException ex) {
                Alerts.emptyExamAlert();
            }
    }

    /**
     * Ustawia typ dla zaznaczonego kodu w polu CodeArea po naciśnięciu przycisku 'usuń'.
     * @param actionEvent
     */
    public void testMarker(ActionEvent actionEvent) {
        changeStyle("test");
    }    
    public void hideMarker(ActionEvent actionEvent) {
        changeStyle("hidden");
    }    
    public void normalMarker(ActionEvent actionEvent) {
        changeStyle("normal");
    }  
    public void gapsMarker(ActionEvent actionEvent) {
        changeStyle("gap");
    }
    public void boldTextMarker(ActionEvent actionEvent) {
        IndexRange ir = text.getSelection(); 
        for (int i = ir.getStart(); i < ir.getEnd(); i++) {            
            text.setStyleClass(i, i+1, BOLD.changeClass(text.getStyleOfChar(i).toString()).getClassName());
        }
    }
    public void italicTextMarker(ActionEvent actionEvent) {
        IndexRange ir = text.getSelection(); 
        for (int i = ir.getStart(); i < ir.getEnd(); i++) {
            text.setStyleClass(i, i+1, ITALIC.changeClass(text.getStyleOfChar(i).toString()).getClassName());
        }
    }
    public void underlineTextMarker(ActionEvent actionEvent) {
        IndexRange ir = text.getSelection(); 
        for (int i = ir.getStart(); i < ir.getEnd(); i++) {
            text.setStyleClass(i, i+1, UNDERLINE.changeClass(text.getStyleOfChar(i).toString()).getClassName());
        }
    }
    public void undoTextMarker(ActionEvent actionEvent) {
        IndexRange ir = text.getSelection(); 
        text.setStyleClass(ir.getStart(), ir.getEnd(), UNDO.getClassName());
    }
    public void monospaceTextMarker(ActionEvent actionEvent) {
        IndexRange ir = text.getSelection(); 
        for (int i = ir.getStart(); i < ir.getEnd(); i++) {
            text.setStyleClass(i, i+1, MONOSPACE.changeClass(text.getStyleOfChar(i).toString()).getClassName());
        }
    }
    
    /**
     * Ustawia typ dla kodu zawartego w polu CodeArea.
     * @param className Nazwa typu do przypisania.
     */
    private void changeStyle(String className) {
        IndexRange ir = code.getSelection(); 
        int end = ir.getEnd();
        String c = code.getText();
        while (end < code.getLength()) {
            if (c.charAt(end) == '\n') {
                break;
            }
            end++;
        }
        code.setStyleClass(ir.getStart(), ir.getEnd(), className);
    }

    /**
     * Wyświetla okno dodawania nowego zadania.
     * @param event
     * @throws Exception
     */
    public void addTask(ActionEvent event) throws Exception {
        Text text = new Text();
        GUIAddTaskController.show();
    }

    /**
     * Usuwa zadanie wskazywane przez aktywną zakładkę.
     * @param event
     * @throws Exception
     */
    public void deleteTask(ActionEvent event) throws Exception {
        if(Exam.getInstance().getTasks().isEmpty()) {
            showTask(false);
        }
        else {
            deleteCurrentTabPaneTab();
        }
    }

    /**
     * Zmienia nazwę aktualnie aktywnej zakładki.
     * @param event
     * @throws Exception
     */
    public void changeTaskName(ActionEvent event) throws Exception {
        status = Status.RENAME;
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Edycja nazwy zadania");
        textInputDialog.setHeaderText("Wpisz nową nazwę");
        Optional<String> result = textInputDialog.showAndWait();
        if(result.isPresent() && result.get().length() > 0) {
            DraggableTab tab = new DraggableTab(result.get());
            Exam.getInstance().getNames().set(exam.idx, result.get());
            tab.setId(Integer.toString(exam.idx));
            tabPane.getTabs().set(exam.idx, tab);
            tabPane.selectionModelProperty().get().select(exam.idx);
        }
        status = Status.SWITCH;
    }

    /**
     * Wyświetla okno dialogowe umożliwiające zmianę odpowiedzi w zadaniu.
     * @param event
     * @throws Exception
     */
    public void changeNumberofAnswers(ActionEvent event) throws Exception {
        int answNum = Integer.MAX_VALUE-1;
        Dialog dialog;
        dialog = new TextInputDialog("MAX");
	dialog.setTitle("Liczba odpowiedzi");
	dialog.setHeaderText("Ile odpowiedzi będzie miało zadanie?");
	
	Optional<String> result = dialog.showAndWait();
	String entered = "MAX";
	 
	if (result.isPresent()) {	 
	    entered = result.get();
            if(!entered.contentEquals("MAX"))
                try{
                    answNum= Integer.valueOf(entered);
                }
                catch(NumberFormatException ex){
                    answNum=Integer.MAX_VALUE-1;
                }
        }
        exam.getTaskAtIndex(exam.idx).getType().getParams().setNoOfAnswers(answNum);
    }

    /**
     * Ustawia widoczność elementów okna głównego.
     * @param visibility Określa żądaną widoczność elementów okna.
     */
    public void showTask(boolean visibility) {
        text.setVisible(visibility);
        textOptions.setVisible(visibility);
        code.setVisible(visibility);
        result.setVisible(visibility);
        executeBtn.setVisible(visibility);
        testExecuteBtn.setVisible(visibility);
        normalMarkerBtn.setVisible(visibility);
        testMarkerBtn.setVisible(visibility);
        hideMarkerBtn.setVisible(visibility);
        changeNameItem.setVisible(visibility);
       
        if(visibility){
            if(exam.getTaskAtIndex(exam.idx).getType().name.contentEquals("ComplexOutput")){
                changeAnswersNum.setVisible(visibility);
            }
            else{
                changeAnswersNum.setVisible(false);
            }
        }
        if(visibility){
            if(exam.getTaskAtIndex(exam.idx).getType().name.contentEquals("Gaps")){
                gapsMarkerBtn.setVisible(visibility);
            }
            else{
                gapsMarkerBtn.setVisible(false);
            }
        }
    }

    /**
     * Odświeża zawartość okna.
     * @param idx Numer zadania które ma zostać wyświetlone.
     */
    public void updateWindow(int idx) {
        if(exam.getTasks().isEmpty()) {  // gdy egzamin nie zawiera żadnych zadań
            showTask(false); // ukryj elementy związane z Taskiem
        }
        else {         
            Task t = exam.getTaskAtIndex(idx);
            exam.idx = idx;

            showTask(true);
            updateText(t.getContent());
            updateCode(t.getText());
            updateResult(t.getResult());
        }
    }

    /**
     * Aktualizuje tekst polecenia.
     * @param content Obiekt klasy Text zawierający informacje o tekście i stanie znaczników.
     */
    public void updateText(Content content) {
        content.creatStyleClassedTextAreaText(text);
        /*this.text.clear();
        if(!text.isEmpty()) {
            int i = 0;
            String line;
            line = text.get(i);
            while (i<text.size()) {
                this.text.appendText(line + "\n");
                i++;
                if(i>=text.size()) break;
                line = text.get(i);
            }
        }*/
    }

    /**
     * Aktualizuje tekst kodu.
     * @param text Obiekt klasy Text zawierający informacje o tekście i stanie znaczników.
     */
    public void updateCode(Text text) {
        text.createCodeAreaText(code);
    }

    /**
     * Aktualizuje tekst zwrócony przez kompilator.
     * @param text Tekst który ma zostać wyświetlony w polu wyjścia kompilatora.
     */
    public void updateResult(String text) {
        this.result.clear();
        this.result.setText(text);
    }

    /**
     * Dodaje nową zakładkę z zadaniem.
     */
    public void addNewTabPaneTab() {
        status = Status.ADD;
        DraggableTab newTab = new DraggableTab("Zadanie " + (exam.maxIdx));
        newTab.setId(Integer.toString(exam.idx));
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
    }

    /**
     * Usuwa aktywną zakładkę wraz z zawartym w niej zadaniem.
     */
    public void deleteCurrentTabPaneTab() {
        status = Status.DELETE;
        exam.deleteTaskAtIndex(exam.idx);
        tabPane.getTabs().remove(exam.idx);
    }

    /**
     * Aktualizuje indeksy zakładek w przypadku zmiany ich organizacji (np. usunięcia jednej z nich).
     */
    public void updateTabPaneTabIndexes() {
        for(int i = 0; i < tabPane.getTabs().size(); i++) {
            tabPane.getTabs().get(i).setId(Integer.toString(i));
        }
    }

    /**
     * Zapisuje treść polecenia w odpowiednie pole obiektu reprezentującego dane zadanie.
     * @param idx Indeks zadania, dla którego ma zostać uaktualnione pole z poleceniem.
     */
    public void saveContent(int idx) {
        Task task = Exam.getInstance().getTaskAtIndex(idx);
        task.getContent().extractContent(text);
       /* String res = "";
        res += text.getText().charAt(0);
        for (int i = 1; i < text.getText().length(); i++) {
            if (text.getStyleOfChar())
        }
        Exam.getInstance().getTaskAtIndex(idx).setContents(Arrays.asList(text.getText().split("\n")));*/
    }

    /**
     * Zapisuje wynik kompilacji w odpowiednie pole obiektu reprezentującego dane zadanie.
     * @param idx Indeks zadania, dla którego ma zostać uaktualnione pole z wynikiem kompilacji.
     */
    public void saveResult(int idx) {
        Exam.getInstance().getTaskAtIndex(idx).setResult(result.getText());
    }

    /**
     * Zapisuje kod wraz ze znacznikami w odpowiednie pole obiektu reprezentującego dane zadanie.
     * W przypadku zadania z lukami dodatkowo generuje odpowiedzi do zadania.
     * @param idx Indeks zadania, dla którego ma zostać uaktualnione pole z kodem.
     */
    public void saveText(int idx) {
        Task task = Exam.getInstance().getTaskAtIndex(idx);
        task.getText().extractText(code);
        if(task.getType().name.equals("Gaps")) {
            task.calculateGapsAnswers(task.getText().getTextParts());
            task.getType().getParams().setNoOfAnswers(task.getAnswers().size());
        }
    }
    
    /**
     * Zapisuje stan bieżącego zadania i generuje plik .xml z egzaminem.
     */
    public void saveCodeAreaToXML() {
        saveText(exam.idx);
        saveContent(exam.idx);
        saveResult(exam.idx);
        Exam.getInstance().save();
    }

    /**
     * Laduje egzamin do programu ze z góry określonego pliku.
     */
    public void loadXMLToCodeArea() {

        Exam.getInstance().load();
        status = Status.DRAG;
        tabPane.getTabs().clear();
        status = Status.SWITCH;
//        int tabsNumber = tabPane.getTabs().size();
        int size = Exam.getInstance().getTasks().size();

        for(int i = 0; i < size; i++) {
            DraggableTab newTab = new DraggableTab(Exam.getInstance().getNames().get(i));
            newTab.setId(Integer.toString(i));
            tabPane.getTabs().add(newTab);
        }

        tabPane.getSelectionModel().select(0);

        Text text = Exam.getInstance().getTaskAtIndex(0).getText();
        text.createCodeAreaText(code);

        Content content = Exam.getInstance().getTaskAtIndex(0).getContent();
        content.creatStyleClassedTextAreaText(this.text);
    }
}