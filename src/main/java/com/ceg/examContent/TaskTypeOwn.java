package com.ceg.examContent;

import java.util.List;

public class TaskTypeOwn extends TaskType{

    public TaskTypeOwn() {
        super();
        name = "OwnType";
    }

    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers){
        answers.clear();
        preparePdfAnswers(task);
    }

    @Override
    public void callExecute(Task task, List<String> output) {
        List<String> code = task.getText().getStandardCompilationCode();
        task.compiler.execute(code, "owntype.cpp", output);
        //task.getType().generateAnswers(task, output, task.getAnswers());
        if (task.getUpdateAnswers()) {
            task.getType().generateAnswers(task, output, task.getAnswers());
        }
        else {
            task.getType().preparePdfAnswers(task);
        }
    }

    @Override
    public void preparePdfAnswers(Task task){
        task.getPdfAnswers().clear();
        for(int i=0; i<this.getNoOfAnswers(); i++){
            String label;
            if (i < task.getLabels().size()) {
                label = task.getLabels().get(i);
            } else {
                label = "";
            }
            task.getPdfAnswers().add(label + " #placeForAnswer");
        }
    }
}