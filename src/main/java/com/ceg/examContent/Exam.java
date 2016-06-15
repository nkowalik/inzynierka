/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    private final static Exam instance = new Exam();
    private int idx;


    public Task getCurrentTask() {
        return tasks.get(idx);
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
        synchronized(this) {
            tasks.add(t);
        }
        setChanged();
        notifyObservers(tasks);
        clearChanged();
    }
    public void addTaskAtIndex(Task t, int idx){
        tasks.add(idx,t);
    }
    public Task getTaskAtIndex(int idx){
        return tasks.get(idx);
    }
    /*zmienić tak, aby kompilowany był cały egzamin*/
    public boolean compileExam() {
        return true;
    }
}
