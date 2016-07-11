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
        name = "SimpleOutput";
        defaultContents = "Podaj co pojawi się na wyjściu w wyniku wykonania programu.";
    }
    
    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers){
        answers.clear();
        try{
            for(int i=1;i<super.getParams().getNoOfAnswers()+1;i++){
                answers.add(output.get(i));
            }
        }
        catch (IndexOutOfBoundsException e) {
            answers.clear();
            System.err.println("IndexOutOfBoundsException: " + e.getMessage());
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
        List<String> code = new ArrayList<>(task.getCode());
        task.compiler.execute2(code, "simple.cpp", output);
        task.getType().generateAnswers(task, output, task.getAnswers());
    }
    
    @Override
    public void preparePdfAnswers(Task task){
        task.getPDFAnswers().clear();
        task.getPDFAnswers().add("Wynik = #placeForAnswer");
    }
}
