/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.examContent;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author marta
 */
public class TaskTypeLineNumbers extends TaskType{
    
    public TaskTypeLineNumbers() {
        super();
        super.params = new TaskParametersLineNumbers();
        name="LineNumbers";
    }

    @Override
    public void generateAnswers(Task task, List<String> output, List<String> answers) {
        answers.clear();
        // jeśli nie nastąpił błąd kompilacji, nie dodaj jedną odpowiedź
        if(output.get(0).contentEquals("Kompilacja przebiegła pomyślnie.")){
            answers.add("Brak błędów kompilacji.");
            this.params.setNoOfAnswers(1);
        }
        else{
            try{
                int answersCnt = 0;
                for(String line: output){
                    if(line.contains("error")){
                        String[] substr = line.split(":");
                        int lineNumber;
                        if(task.compiler.osName.indexOf("win") >= 0) // windows uzywa dodatkowego znaku ':' po nazwie dysku
                            lineNumber = Integer.parseInt(substr[2])-1;
                        else
                            lineNumber = Integer.parseInt(substr[1])-1;
                        String[] codeLine = task.getCode().get(lineNumber).split("//");
                        answers.add(codeLine[1]);
                        answersCnt++;
                    }               
                }
                this.params.setNoOfAnswers(answersCnt);
            }
           catch (IndexOutOfBoundsException e) {
                answers.clear();
                this.params.setNoOfAnswers(0);
                System.err.println("IndexOutOfBoundsException: " + e.getMessage());

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText("Nastąpił błąd podczas generowania odpowiedzi.");
                alert.setContentText("Sprawdź poprawność kodu.");

                alert.showAndWait();
            }
        }
        preparePdfAnswers(task);
        }
        preparePdfAnswers(task);
    }

    @Override
    public void callExecute(Task task, List<String> output) {
        List<String> code = new ArrayList<>(task.getCode());
        task.compiler.execute(code, "linenumbers.cpp", output);
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
