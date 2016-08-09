/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.examContent;

import com.ceg.compiler.GCC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private List<String> PDFAnswers;
    private String result;
    private TaskType type;
    public GCC compiler;
    
    
    public Task(TaskType tt){  
        this.type = tt;
        setCode(Arrays.asList(""));
        this.answers = new ArrayList<>();
        /* to tylko na razie */
        PDFAnswers = new ArrayList<>();
       // type.preparePdfAnswers(this);
        compiler = new GCC();
        PDFCode = new ArrayList<>();
        contents = new ArrayList<>();
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
    public List<String> getPDFAnswers() {
        return PDFAnswers;
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
    public String getResult() {
        return result;
    }
    public void setResult(String newResult) {
        result = newResult;
    }
    
}
