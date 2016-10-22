package com.ceg.examContent;

import com.ceg.compiler.GCC;
import com.ceg.gui.Alerts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Klasa Task zawiera dane opisujące pojedyncze zadanie:
 * contents - treść polecenia
 * answers - odpowiedzi do zadania
 * pdfAnswers - odpowiedzi do zadania, które pojawią się w pliku .pdf
 * result - wynik kompilacji
 * type - typ zadania
 * text - kod zadania
 * compiler - kompilator przypisany do danego zadania
 */
@XmlRootElement
public class Task {
    private Content content;
    private List<String> answers;
    private List<String> pdfAnswers;
    private String result;
    private TaskType type;
    private Text text;
    public GCC compiler;

    /**
     * Tworzy zadanie, dokonuje inicjalizacji zmiennych.
     */
    public Task() {
        this.answers = new ArrayList<>();
        pdfAnswers = new ArrayList<>();
        compiler = new GCC();
        content = new Content();
        text = new Text();
    }

    /**
     * Tworzy zadanie ze zdefiniowanym typem.
     * @param tt Typ który ma zostać przypisany do zadania.
     */
    public Task(TaskType tt){
        this();
        this.type = tt;
    }
    public Content getContent(){
        return content;
    }
    public void setContent(Content content){
        this.content = content;
    }
    public List<String> getAnswers(){
        return answers;
    }
    public void setAnswers(List<String> answers) { this.answers = answers; }
    public List<String> getPdfAnswers() {
        return pdfAnswers;
    }
    public void setPdfAnswers(List<String> pdfAnswers) { this.pdfAnswers = pdfAnswers; }
    public TaskType getType(){
        return type;
    }
    public void setType(TaskType type){
        this.type = type;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public Text getText() {
        return text;
    }
    public void setText(Text text) {
        this.text = text;
    }

    /**
     * Wyznacza odpowiedzi do zadania z lukami i zapisuje je w klasie Task.
     * @param textParts Lista części kodu reprezentująca kod przypisany do zadania, podzielony ze względu na typ.
     */
    public void calculateGapsAnswers(List<TextPart> textParts) {
        List<TextPart> code = new ArrayList(textParts);

        for(int i = 0; i < code.size(); i++) {
            if(!code.get(i).getType().equals("[gap]")) {
                code.remove(i);
                i--;
            }
        }

        List<String> output = new ArrayList();
        for (TextPart part : code) {
            String[] list = part.toString().split("\n");
            for (String element : list) {
                output.add(element);
            }
        }
        answers = output;
    }

    public void save(String filename) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Task.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this, new File(filename));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public boolean load(String filename) {
        try {
            JAXBContext context = JAXBContext.newInstance(Task.class);
            Unmarshaller un = context.createUnmarshaller();
            Task task = (Task)un.unmarshal(new File(filename));
            this.setAnswers(task.answers);
            this.setContent(task.content);
            this.setPdfAnswers(task.pdfAnswers);
            this.setResult(task.result);
            this.setText(task.text);
            this.setType(task.type);
        } catch (JAXBException e) {
            Alerts.wrongFileContentAlert();
            return false;
        }
        return true;
    }
}
