/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.compiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marta
 */
public class GccWin extends GCC{
    private Process console;
    private ProcessBuilder builder = null;
    private final static GccWin instance = new GccWin();
    
    public static GccWin getInstance() {
        return instance;
    }
    
    public GccWin(){
        super();
        builder = new ProcessBuilder(new String[]{"cmd.exe"});
    }
     /**
     * Tworzy proces konsoli
     * 
     * @return true jeśli operacja się powiedzie, false w przypadku błędu
     */
    public boolean startConsole(){
        try {
            console = builder.start();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(GccWin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
     /**
     * Kończy proces konsoli
     * 
     * @return true jeśli operacja się powiedzie, false w przypadku błędu
     */
    public boolean stopConsole(){
        return true;
    }
    
    @Override
    public boolean compile(List<String> output) {
        if(console == null || !console.isAlive()){
            startConsole();
        }
        if (super.file.exists() && console.isAlive()) {
            try {
                //(new String[]{"cmd.exe", "/c", "g++", "-o", this.executableName, this.cppName});
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(console.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(console.getErrorStream()));
                
                writer.write("g++ -o " + super.executableName + " " + super.cppName);
                writer.newLine();
                writer.flush();          
                
                String line = null;
                while (reader.ready() && (line = reader.readLine()) != null) {
                    output.add(line);
                }
                if (output.isEmpty())
                    return true;
                else
                    return false;

            } catch (Exception err) {
                err.printStackTrace();
                return false;
            }
        } else
            return false;
    }
    
    @Override
    public void execute(List<String> lines, String name, List<String> output) {
        if(createFile(lines, name)) {
            if(compile(output)) {
                try {
                    output.add("Kompilacja przebiegła pomyślnie.");
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(console.getOutputStream()));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(console.getInputStream()));
                    while(reader.ready()){
                        reader.readLine();
                    }
                    writer.write(super.executableName);
                    writer.newLine();
                    writer.flush();                    

                    String line = null;
                    while(reader.ready() && (line = reader.readLine()) != null) {
                        output.add(line);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GccWin.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else {
            output.add("Błąd. Pusty plik wejściowy.");
        }
    }
    
}
