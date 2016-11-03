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
        task.getType().generateAnswers(task, output, task.getAnswers());
    }

    @Override
    public void preparePdfAnswers(Task task){
        task.getPdfAnswers().clear();
        task.getPdfAnswers().add("#placeForAnswer");
    }
}