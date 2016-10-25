package com.ceg.examContent;

import java.util.List;

public class TaskTypeSimpleOutput extends TaskType{

    public TaskTypeSimpleOutput() {
        super();
        super.params = new TaskParametersSimpleOutput();
        name = "SimpleOutput";
        defaultContents = "Podaj co pojawi się na wyjściu w wyniku wykonania programu.";
    }
    
    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers){
        answers.clear();
        try{
            for(int i=1;i<super.getParams().getNoOfAnswers()+1;i++){
                answers.add(output.get(i));
            }
        }
        catch (IndexOutOfBoundsException e) {
            answers.clear();
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