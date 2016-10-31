package com.ceg.examContent;

import java.util.ArrayList;
import java.util.List;

public class TaskTypeGaps extends TaskType{
   
    
    public TaskTypeGaps() {
        super();
        name="Gaps";
        this.setNoOfAnswers(0);
    }

    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers) {
        if(!output.get(0).contentEquals("Kompilacja przebiegła pomyślnie.")){
            answers.add("Brak");
            this.setNoOfAnswers(1);
        }
        else{
            
        }
        preparePdfAnswers(task);
    }

    @Override
    public void callExecute(Task task, List<String> output) {
        List<String> code = new ArrayList<>(task.getText().getStandardCompilationCode());
        task.compiler.execute(code, "gaps", output);
        task.getType().generateAnswers(task, output, task.getAnswers());
    }

    @Override
    public void preparePdfAnswers(Task task) {
        task.getPdfAnswers().clear();
        for(int i=0;i<this.getNoOfAnswers();i++){
            task.getPdfAnswers().add("#placeForAnswer");
        }
    }
}
