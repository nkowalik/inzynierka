/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.examContent;

import com.ceg.compiler.CodeParser;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marta
 */
public class TaskTypeMultipleOutput extends TaskType{
    
    public TaskTypeMultipleOutput() {
        super.params = new TaskParametersMultipleOutput();
        defaultContents = "Podaj co pojawi się na wyjściu w wyniku kolejnych wywołań funkcji.";
    }
    
    @Override
    public void generateAnswers(List<String> output, List<String> answers){
        for(int i=0;i<super.getParams().getNoOfAnswers();i++){
            answers.add(output.get(i));
        }
    }

    @Override
    public void callCompile(Task task, List<String> output) {
        CodeParser.addNewlineAfterEachCout((ArrayList<String>)task.getCode(), (ArrayList<String>)task.getTestCode());
        task.compiler.createFile(task.getTestCode(), "multiple.cpp");
        task.compiler.compile(output);
    }

    @Override
    public void callExecute(Task task, List<String> output) {
        task.compiler.execute(output);
    }
}
