package com.ceg.examContent;

import com.ceg.compiler.CodeParser;

import java.util.List;

public class TaskTypeReturnedValue extends TaskType{

    public TaskTypeReturnedValue() {
        super();
        name = "ReturnedValue";
        command = "Zwrócona liczba";
    }

    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers) {
        answers.clear();
        setNoOfAnswers(Integer.MAX_VALUE-1);
        // jeśli nastąpił błąd kompilacji, wygeneruj odpowiedź: "Błąd"
        if(!output.get(0).contentEquals("Kompilacja przebiegła pomyślnie.")){
            answers.add("Błąd");
            this.setNoOfAnswers(1);
        }
        else{          
            try{
                int i=0;
                for(String line: output){
                    if(i<super.getNoOfAnswers()+1){
                        if(i>0)
                            answers.add(line);
                        i++;
                    }
                }
                this.setNoOfAnswers(i-1);
            }
            catch (IndexOutOfBoundsException e) {
                answers.clear();
                this.setNoOfAnswers(0);
                System.out.println("Cannot generate answers. Error caused by: " + e.toString());
            }
        }
        preparePdfAnswers(task);
    }

    @Override
    public void callExecute(Task task, List<String> output) {
        List<String> code = task.getText().getStandardCompilationCode();
        CodeParser.addNewlineAfterEachCout(code);
        task.compiler.execute(code, "retvalue", output);
        task.getType().generateAnswers(task, output, task.getAnswers());
    }

    @Override
    public void preparePdfAnswers(Task task) {
        task.getPdfAnswers().clear();
        for(int i=0;i<this.getNoOfAnswers();i++){
            task.getPdfAnswers().add(" #placeForAnswer");
        }
    }
}
