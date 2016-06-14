/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.examContent;

import com.ceg.compiler.GCC;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author marta
 */

/* Klasa zawierająca dane pojedynczego zadania: typ (type), wykonywany kod (code), 
   kod z częścią zamarkowaną jako testowa (testCode), kod, widoczny dla studenta na arkuszu (PDFCode), 
   treść polecenia (contents) */
public class Task {
    private List<String> code;
    private List<String> testCode;
    private List<String> PDFCode;
    private List<String> contents;
    private List<String> answers;
    private TaskType type;
    public GCC compiler;
    
    
    public Task(){
        List<String> defaultCode = new ArrayList<>();
        List<String> defaultContents = new ArrayList<>();
        compiler = new GCC();
        try {
            Scanner s = new Scanner(new File("polecenie.txt"));
            while (s.hasNext()) {
                defaultContents.add(s.nextLine());
            }
            s.close();

            Scanner s1 = new Scanner(new File("test.cpp"));
            for (int i = 0; s1.hasNext(); i++) {
                defaultCode.add(s1.nextLine());
            }

            s1.close();
            contents = defaultContents;
            code = defaultCode;
            testCode = defaultCode;
            PDFCode = defaultCode;

        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
    }
    public List<String> getCode(){
        return code;
    }
    public List<String> getTestCode(){
        return testCode;
    }
    public List<String> getPDFCode(){
        return PDFCode;
    }
    public void setCode(List<String> newCode){
        code = newCode;
    }
    public void setTestCode(List<String> newCode) {
        testCode = newCode;
    }
    public void setPDFCode(List<String> newCode) {
        PDFCode = newCode;
    }
    public List<String> getContents(){
        return contents;
    }
    public void setContents(List<String> newContents){
        contents = newContents;
    }
     public List<String> getAnswers(){
        return answers;
    }
    public void setAnswers(List<String> newAnswers){
        answers = newAnswers;
    }
    public TaskType getType(){
        return type;
    }
    public void setType(TaskType newType){
        type = newType;
    }
    
}
