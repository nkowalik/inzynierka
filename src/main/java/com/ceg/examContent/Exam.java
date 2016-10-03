package com.ceg.examContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 *
 * @author marta
 */
/*
    klasa Exam stanowi kontekst dla modelu danych aplikacji, dla każdego wywołania aplikacji istnieje jedna statyczna instancja
    Exam jest zbiorem obiektów klasy Task
*/
public class Exam extends Observable {
    private ArrayList<Task> tasks;
    private int compilationProgress = -1;
    private List<String> outputList = new ArrayList<>();
    
    private final static Exam instance = new Exam();
    public int idx;


    public Exam() {
    }

    public static Exam getInstance() {
        return instance;
    }
    public void init(){
         tasks = new ArrayList<>();
         idx = 0;
    }
    
    public boolean compile() {
        List<String> output = new ArrayList<>();
        compilationProgress = -1;
        outputList.clear();
        for (Task i : tasks) {
            output.clear();
            i.getType().callExecute(i, output);
            compilationProgress++;
            outputList.add("Zadanie " + (compilationProgress+1) + " : " + output.get(0) + "\n");
            if (!output.get(0).contentEquals("Kompilacja przebiegła pomyślnie.")) {
                output.remove(0);
                output.stream().forEach((s) -> {
                    outputList.add(s + "\n");
                });
                return false;
            }            
        }
        compilationProgress++;
        return true;
    }
        
    public List<Task> getTasks(){
        return tasks;
    }
    public void setTasks(List<Task> newTasks){
        tasks = (ArrayList<Task>) newTasks;
    }
    public void addTask(Task t){
        tasks.add(t);
        idx = tasks.size() - 1;
    }
    public Task getCurrentTask(){
        return tasks.get(idx);
    }
    public Task getTaskAtIndex(int idx){
        return tasks.get(idx);
    }
    public void deleteTaskAtIndex(int idx) {
        tasks.remove(idx);
    }
    public int getCompilationProgress() {
        return compilationProgress;
    }
    public List<String> getOutputList() {
        return outputList;
    }
}
