package com.ceg.examContent;

import com.ceg.utils.Alerts;

import java.util.List;

public class TaskTypeSimpleOutput extends TaskType{

    public TaskTypeSimpleOutput() {
        super();
        super.setNoOfAnswers(1);
        name = "SimpleOutput";
        command = "Wyjście programu";
        defaultContents = "Podaj co pojawi się na wyjściu w wyniku wykonania programu.";
    }

    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers){
        answers.clear();
        try{
            answers.add(output.get(1));
            
        }
        catch (IndexOutOfBoundsException e) {
            answers.clear();
            Alerts.generatingAnswersErrorAlert();
            System.err.println("IndexOutOfBoundsException: " + e.getMessage());
        }
         preparePdfAnswers(task);
    }

    @Override
    public void callExecute(Task task, List<String> output) {
        List<String> code = task.getText().getStandardCompilationCode();
        task.compiler.execute(code, "simple", output);
        task.getType().generateAnswers(task, output, task.getAnswers());
    }
    
    @Override
    public void preparePdfAnswers(Task task){
        task.getPdfAnswers().clear();
        task.getPdfAnswers().add("Wynik = #placeForAnswer");
    }
}