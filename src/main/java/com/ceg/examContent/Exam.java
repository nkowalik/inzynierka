/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.examContent;

import com.ceg.pdf.PDFSettings;
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
    public PDFSettings pdfSettings;
    
    private final static Exam instance = new Exam();
    public int idx;


    public Exam() {
        pdfSettings = new PDFSettings("Times New Roman", "Courier", 10, 10, "student", "egzamin.pdf");
    }
    
    public Task getLastTask() {
        return tasks.get(tasks.size() - 1);
    }
    public static Exam getInstance() {
        return instance;
    }
    public void init(){
         tasks = new ArrayList<>();
         idx = 0;
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
        if(idx != 0)
            this.idx--;
    }
    /*
    public void setCurrentTaskIndex(int idx) {
        this.idx = idx;
    }
    */
    /*zmienić tak, aby kompilowany był cały egzamin*/
    public boolean compileExam() {
        return true;
    }
}
