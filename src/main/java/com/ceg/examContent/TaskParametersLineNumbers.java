/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.examContent;

import java.util.Optional;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;

/**
 *
 * @author marta
 */
public class TaskParametersLineNumbers extends TaskParameters{
    
     public TaskParametersLineNumbers() {
        //askForParams();
        //super.setNoOfAnswers(1);
    }
    
    private void askForParams(){
        Dialog dialog;
        dialog = new TextInputDialog("1");
	dialog.setTitle("Liczba odpowiedzi");
	dialog.setHeaderText("Ile odpowiedzi będzie miało zadanie?");
	 
	Optional<String> result = dialog.showAndWait();
	String entered = "none.";
	 
	if (result.isPresent()) {
	 
	    entered = result.get();
            super.setNoOfAnswers(Integer.valueOf(entered));
        }
    }
}
