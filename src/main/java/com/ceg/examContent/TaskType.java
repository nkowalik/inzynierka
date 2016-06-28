/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.examContent;

import java.util.List;

/**
 *
 * @author marta
 */

/* 
   * klasa zawierająca dane typowe dla konkretnego typu zadania: domyślną treść polecenia 
   * (defaultContents), parametry i metody potrzebne do wygenerowania odpowiedzi
*/ 
abstract public class TaskType {
    String defaultContents;
    protected TaskParameters params;
    
    
    public abstract void generateAnswers(List<String> output, List<String> answers);
    public abstract void callCompile(Task task, List<String> output);
    public abstract void callExecute(Task task, List<String> output);
    public abstract void preparePdfAnswers(Task task);
    
    public String getDefaultContents() {
        return defaultContents;
    }

    public void setDefaultContents(String defaultContents) {
        this.defaultContents = defaultContents;
    }

    public TaskParameters getParams() {
        return params;
    }

    public void setParams(TaskParameters params) {
        this.params = params;
    }
       
}
