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
    @XmlElement
    private ArrayList<String> names;
    private final static Exam instance = new Exam();

    /**
     * Indeks aktualnego zadania (wskazywanego przez zakładkę).
     */
    public int idx;

    /**
     * Aktualny indeks dodawanego zadania.
     */
    public int maxIdx = 0;

    public Exam() {
    }
    public static Exam getInstance() {
        return instance;
    }
    public void init(){
         tasks = new ArrayList<>();
         names = new ArrayList<>();
         idx = 0;
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
    }

    public void changeTasksOrder(int oldIndex, int newIndex) {
        Task task = tasks.get(oldIndex);
        tasks.remove(oldIndex);
        tasks.add(newIndex, task);

        String name = names.get(oldIndex);
        names.remove(oldIndex);
        names.add(newIndex, name);
    }

    // todo zmienić tak, aby kompilowany był cały egzamin*/
    public boolean compileExam() {
        return true;
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
            this.names = exam.names;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
