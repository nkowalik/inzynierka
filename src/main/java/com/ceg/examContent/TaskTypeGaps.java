package com.ceg.examContent;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marta
 */
public class TaskTypeGaps extends TaskType{
   
    
    public TaskTypeGaps() {
        super();
        name="Gaps";
        params = new TaskParametersGaps();
        this.params.setNoOfAnswers(0);
    }

    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers) {
        if(!output.get(0).contentEquals("Kompilacja przebiegła pomyślnie.")){
            answers.add("Brak");
            this.params.setNoOfAnswers(1);
        }
        else{
            
        }
        preparePdfAnswers(task);
    }

    @Override
    public void callExecute(Task task, List<String> output) {
        List<String> code = new ArrayList<>(task.getCode());
        task.compiler.execute(code, "gaps.cpp", output);
        task.getType().generateAnswers(task, output, task.getAnswers());
    }

    @Override
    public void preparePdfAnswers(Task task) {
        task.getPDFAnswers().clear();
        for(int i=0;i<this.params.getNoOfAnswers();i++){
            task.getPDFAnswers().add("#placeForAnswer");   
        }
    }
    
}
