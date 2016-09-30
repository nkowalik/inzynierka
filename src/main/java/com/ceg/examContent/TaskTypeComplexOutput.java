package com.ceg.examContent;

import com.ceg.compiler.CodeParser;
import com.ceg.utils.Alerts;
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
        answers.clear();
        // jeśli nastąpił błąd kompilacji, wygeneruj odpowiedź: "Błąd"
        if(!output.get(0).contentEquals("Kompilacja przebiegła pomyślnie.")){
            answers.add("Błąd");
            this.params.setNoOfAnswers(1);
        }
        else{          
            try{
                int i=0;
                for(String line: output){
                    if(i<super.getParams().getNoOfAnswers()+1){
                        if(i>0)
                            answers.add(line);
                        i++;
                    }
                }
                this.params.setNoOfAnswers(i-1);
            }
            catch (IndexOutOfBoundsException e) {
                answers.clear();
                this.params.setNoOfAnswers(0);
                System.err.println("IndexOutOfBoundsException: " + e.getMessage());

                Alerts.generatingAnswersErrorAlert();
            }
        }
        preparePdfAnswers(task);
    }

    @Override
    public void callExecute(Task task, List<String> output) {
        List<String> code = new ArrayList<>(task.getCode());
        CodeParser.addNewlineAfterEachCout(code);
        task.compiler.execute(code, "multiple.cpp", output);
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