/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.examContent;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marta
 */
public class TaskTypeSimpleOutput extends TaskType{

    public TaskTypeSimpleOutput() {
        super();
        super.params = new TaskParametersSimpleOutput();
        defaultContents = "Podaj co pojawi się na wyjściu w wyniku wykonania programu.";
    }
    
    @Override
    public void generateAnswers(List<String> output, List<String> answers){
        for(int i=0;i<super.getParams().getNoOfAnswers();i++){
            answers.add(output.get(i));
        }
    }

    @Override
    public void callCompile(Task task, List<String> output) {
        List<String> code = new ArrayList<>(task.getCode());
        task.compiler.createFile(code, "simple.cpp");
        task.compiler.compile(output);
    }

    @Override
    public void callExecute(Task task, List<String> output) {
        task.compiler.execute(output);
        task.getType().generateAnswers(output, task.getAnswers());
    }
    
    @Override
    public void preparePdfAnswers(Task task){
        task.getPDFAnswers().clear();
        task.getPDFAnswers().add("Wynik = #placeForAnswer");
    }
}
