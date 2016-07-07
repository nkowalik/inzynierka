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
        super();
        super.params = new TaskParametersComplexOutput();
        name = "ComplexOutput";
        defaultContents = "Podaj co pojawi się na wyjściu w wyniku kolejnych wywołań funkcji.";
    }
    
    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers){
        for(int i=1;i<super.getParams().getNoOfAnswers()+1;i++){
            answers.add(output.get(i));
        }
    }

    @Override
    public void callCompile(Task task, List<String> output) {
        List<String> code = new ArrayList<>(task.getCode());
        CodeParser.addNewlineAfterEachCout(code);
        task.compiler.createFile(code, "multiple.cpp");
        task.compiler.compile(output);
    }

    @Override
    public void callExecute(Task task, List<String> output) {
        List<String> code = new ArrayList<>(task.getCode());
        CodeParser.addNewlineAfterEachCout(code);
        task.compiler.execute2(code, "multiple.cpp", output);
        task.getType().generateAnswers(task, output, task.getAnswers());
    }
    
    @Override
    public void preparePdfAnswers(Task task){
        task.getPDFAnswers().clear();
        for(int i=0;i<this.params.getNoOfAnswers();i++){
            task.getPDFAnswers().add(" #placeForAnswer");   
        }
    }
}
