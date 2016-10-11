package com.ceg.examContent;

import com.ceg.compiler.GCC;
import java.util.ArrayList;
import java.util.List;

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
public class Task {
    private List<String> contents;
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
        contents = new ArrayList<>();
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
    public List<String> getContents(){
        return contents;
    }
    public void setContents(List<String> contents){
        this.contents = contents;
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
}