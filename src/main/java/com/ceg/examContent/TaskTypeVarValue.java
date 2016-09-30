/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.examContent;

import com.ceg.compiler.CodeParser;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;

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

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText("Nastąpił błąd podczas generowania odpowiedzi.");
                alert.setContentText("Sprawdź poprawność kodu.");

                alert.showAndWait();
            }
        }
        preparePdfAnswers(task);
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
        task.getPDFAnswers().clear();
        for(int i=0;i<this.params.getNoOfAnswers();i++){
            task.getPDFAnswers().add(" #placeForAnswer");   
        }
    }
    
}
