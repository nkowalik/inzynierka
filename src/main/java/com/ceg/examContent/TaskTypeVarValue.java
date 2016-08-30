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
public class TaskTypeVarValue extends TaskType{

    public TaskTypeVarValue() {
        super();
        super.params = new TaskParametersVarValue();
        name = "VarValue";
    }

    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void callExecute(Task task, List<String> output) {
        List<String> code = new ArrayList<>(task.getCode());
        CodeParser.addNewlineAfterEachCout(code);
        task.compiler.execute(code, "varvalue.cpp", output);
        task.getType().generateAnswers(task, output, task.getAnswers());
    }

    @Override
    public void preparePdfAnswers(Task task) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
