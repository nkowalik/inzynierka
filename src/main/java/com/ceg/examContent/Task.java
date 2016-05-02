/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.examContent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author marta
 */

// Klasa zawierająca dane pojedynczego zadania: typ (type), kod (code), treść polecenia (contents)
public class Task {
    private ArrayList<String> code;
    private ArrayList<String> contents;
    private int type;
    
    public Task(){
        ArrayList<String> defaultCode = new ArrayList<>();
        ArrayList<String> defaultContents = new ArrayList<>();

        try {
            Scanner s = new Scanner(new File("polecenie.txt"));
            while (s.hasNext()) {
                defaultContents.add(s.nextLine());
            }
            s.close();

            Scanner s1 = new Scanner(new File("kod.txt"));
            for (int i = 0; s1.hasNext(); i++) {
                defaultCode.add(s1.nextLine());
            }

            s1.close();

        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }
    public ArrayList<String> getCode(){
        return code;
    }
    public void setCode(List<String> newCode){
        code = (ArrayList<String>)newCode;
    }
    public ArrayList<String> getContents(){
        return contents;
    }
    public void setContents(List<String> newContents){
        contents = (ArrayList<String>)newContents;
    }
    public int getType(){
        return type;
    }
    public void setType(int newType){
        type = newType;
    }
    
}
