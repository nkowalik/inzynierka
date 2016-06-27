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
public class TaskTypeComplexOutput extends TaskType{
    
    public TaskTypeComplexOutput() {
        super.params = new TaskParametersComplexOutput();
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
        CodeParser.addNewlineAfterEachCout(task.getCode(), task.getTestCode());
        task.compiler.createFile(task.getCode(), "multiple.cpp");
        task.compiler.compile(output);
    }

    @Override
    public void callExecute(Task task, List<String> output) {
        task.compiler.execute(output);
    }
}
