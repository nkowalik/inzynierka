package com.ceg.examContent;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javafx.scene.control.TabPane;
import javax.xml.bind.annotation.XmlElement;

/**
 * Klasa Exam stanowi kontekst dla modelu danych aplikacji, dla każdego wywołania aplikacji istnieje jedna statyczna instancja.
 * Exam jest zbiorem obiektów klasy Task.
 */
@XmlRootElement
public class Exam extends Observable {
    private ArrayList<Task> tasks;
    private int compilationProgress = -1;
    private List<String> outputList = new ArrayList<>();
    @XmlElement
    private ArrayList<String> names;
    private static Exam instance;

    /**
     * Indeks aktualnego zadania (wskazywanego przez zakładkę).
     */
    public int idx;

    /**
     * Aktualny indeks dodawanego zadania.
     */
    public int maxIdx;

    private Exam() {
    }
    public static Exam getInstance() {
        if (instance == null){
            synchronized(Exam.class){
                if(instance == null)
                    instance = new Exam();
            }
        }
        return instance;
    }
    public void init(){
        tasks = new ArrayList<>();
        names = new ArrayList<>();
        idx = 0;
        maxIdx = 0;
    }
    
    public boolean compile() {
        List<String> output = new ArrayList<>();
        
        clearOutputList();
        for (Task i : tasks) {
            output.clear();
            i.getType().callExecute(i, output);
            i.setResult(String.join("\n", output));
            this.incCompilationProgress();
            addToOutputList("Zadanie " + (getCompilationProgress()+1) + " : " + output.get(0) + "\n");
            if (!output.get(0).contentEquals("Kompilacja przebiegła pomyślnie.")) {
                output.remove(0);
                output.stream().forEach((s) -> {
                    addToOutputList(s + "\n");
                });
               // return false;
            }            
        }
        this.incCompilationProgress();
        return true;
    }
        
    public List<Task> getTasks(){
        return tasks;
    }
    public void setTasks(List<Task> newTasks){
        tasks = (ArrayList<Task>) newTasks;
    }
    public List<String> getNames() {
        return names;
    }
    public void setNames(ArrayList<String> tabsNames) {
        this.names = tabsNames;
    }
    /**
     * Dodaje nowe zadanie do egzaminu.
     * @param t Zadanie które ma zostać dodane do egzaminu.
     */
    public void addTask(Task t){
        tasks.add(t);
        idx = tasks.size() - 1;
        maxIdx++;
        names.add("Zadanie " + maxIdx);
    }

    /**
     * Pobiera z egzaminu zadanie wskazywane przez aktualną zakładkę.
     * @return Odczytane zadanie.
     */
    public Task getCurrentTask(){
        return tasks.get(idx);
    }

    /**
     * Pobiera zadanie znajdujące się na podanej pozycji w egzaminie.
     * @param idx Liczba określająca numer zadania które ma zostać pobrane z egzaminu.
     * @return Odczytane zadanie.
     */
    public Task getTaskAtIndex(int idx){
        return tasks.get(idx);
    }

    /**
     * Usuwa zadanie znajdujące się na podanej pozycji w egzaminie.
     * @param idx Liczba określająca numer zadania które ma zostać usunięte.
     */
    public void deleteTaskAtIndex(int idx) {
        tasks.remove(idx);
        names.remove(idx);
    }
    
    public synchronized void incCompilationProgress(){
        compilationProgress++;
    }
    
    public synchronized int getCompilationProgress() {
        return compilationProgress;
    }
    
    public synchronized void clearCompilationProgress(){
        compilationProgress = -1;
    }
    
    public synchronized List<String> getOutputList() {
        return outputList;
    }
    
    private synchronized void addToOutputList(String str){
        outputList.add(str);
    }
    
    private synchronized void clearOutputList(){
        outputList.clear();
    }
    
    public void changeTasksOrder(int oldIndex, int newIndex) {
        Task task = tasks.get(oldIndex);
        tasks.remove(oldIndex);
        tasks.add(newIndex, task);

        String name = names.get(oldIndex);
        names.remove(oldIndex);
        names.add(newIndex, name);
    }

    /**
     * Zapisuje egzamin ze z góry zdefiniowaną nazwą.
     */
    // todo umożliwić ustalenie nazwy na etapie tworzenia egzaminu
    public void save() {
        try {
            JAXBContext jc = JAXBContext.newInstance(Exam.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this, new File("arkusz.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * Odczytuje egzamin ze z góry zdefiniowaną nazwą.
     */
    // todo umożliwić wybór konkretnego arkusz oraz obsłużyć wyjątek braku arkusza o podanej nazwie
    public void load() {
        try {
            JAXBContext context = JAXBContext.newInstance(Exam.class);
            Unmarshaller un = context.createUnmarshaller();
            Exam exam = (Exam)un.unmarshal(new File("arkusz.xml"));
            this.setTasks(exam.tasks);
            this.idx = exam.idx;
            this.maxIdx = exam.maxIdx;
            this.names = exam.names;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
