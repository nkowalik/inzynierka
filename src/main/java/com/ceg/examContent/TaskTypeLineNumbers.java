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
public class TaskTypeLineNumbers extends TaskType{
    
    public TaskTypeLineNumbers() {
        super();
        super.params = new TaskParametersLineNumbers();
        name="LineNumbers";
    }

    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers) {
        for(String line: output){
            if(line.contains("error")){
                String[] substr = line.split(":");
                int lineNumber = Integer.parseInt(substr[1])-1;
                String[] codeLine = task.getCode().get(lineNumber).split("//");
                answers.add(codeLine[1]);
            }               
        }
    }

    @Override
    public void callCompile(Task task, List<String> output) {
        task.compiler.createFile(task.getCode(), "linenumbers.cpp");
        task.compiler.compile(output);
    }

    @Override
    public void callExecute(Task task, List<String> output) {
        List<String> code = new ArrayList<>(task.getCode());
        task.compiler.execute2(code, "linenumbers.cpp", output);
        task.getType().generateAnswers(task, output, task.getAnswers());
    }

    @Override
    public void preparePdfAnswers(Task task) {
        task.getPDFAnswers().clear();
        for(int i=0;i<this.params.getNoOfAnswers();i++){
            task.getPDFAnswers().add(" #placeForAnswer");   
        }
    }
    
}
