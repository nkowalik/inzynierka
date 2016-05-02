/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.SystemUtils;

/**
 *
 * @author Pawel
 */

/*

obsluga:
createFile(lista linii, nazwa pliku wyjsciowego z rozszerzeniem)
addNewLine itp. z klasy CodeParser - modyfikacja utworzonego pliku
compile(lista linii wyjsciowych) - kompilacja wczesniej utworzonego pliku, wazne! - jego sciezka pozostaje taka sama, nalezy operowac na tym wygenerowanym pliku
execute(lista linii wyjsciowych)

*/


public class GCC {
    
    private String path;
    private File file;
    private String cppName;
    private String executableName;
    
    public GCC() {
        String name = System.getProperty("os.name").toLowerCase();
        
        CodeSource codeSource = GCC.class.getProtectionDomain().getCodeSource();
        File jarFile;
        try {
            jarFile = new File(codeSource.getLocation().toURI().getPath());
            path = jarFile.getParentFile().getPath();
        } catch (URISyntaxException ex) {
            Logger.getLogger(GCC.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    // nieważne czy kod zostanie wprowadzony z pliku czy ręcznie
    // program wygeneruje plik .cpp dla konkretnego zadania
    // nazwa z gory zdefiniowana przy wywolaniu
    
    // utworzony plik mozna modyfikowac funkcja parsujaca, nie jest sprawdzane, czy zostal on zmieniony od czasu utworzenia
    
    public void createFile(List<String> lines, String name) {
        try {
            Files.write(Paths.get(this.path + "/" + name), lines, Charset.forName("UTF-8"));
            file = new File(this.path + "/" + name);
            
        } catch (IOException ex) {
            Logger.getLogger(GCC.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.cppName = this.file.getAbsolutePath();
        this.executableName = this.path.toString() + "/" + this.file.getName().substring(0, this.file.getName().lastIndexOf("."));
        
    }
    
    // kompilacja pliku .cpp stworzonego przy użyciu funkcji createFile
    
    public void compile(List<String> output) {
        if(file.exists()) {
            try {
                ProcessBuilder builder = null;
                if(SystemUtils.IS_OS_WINDOWS) {
                        builder = new ProcessBuilder("cmd.exe", "/c", "g++ " + this.cppName + " -o " + this.executableName + ".exe");
                }
                else if(SystemUtils.IS_OS_LINUX) {
                        builder = new ProcessBuilder("g++", this.cppName, "-o", this.executableName);
                }
                else {
                    System.out.println("Nieobsługiwany system operacyjny");
                }
                Process p = builder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line = null;
                while((line = reader.readLine()) != null) {
                    output.add(line);
                }
                p.waitFor();
                p.destroy();

            }
            catch(Exception err) {
                err.printStackTrace();
            }
        }
        
        
    }
    
    // wykonanie wczesniej przygotowanego pliku
    // metoda niestatyczna aby zapobiec wykonywaniu pliku przed jego kompilacją
    // zawsze mozna uruchomic tylko ostatnio skompilowany plik
    // tym samym dla kazdego zadania mozna miec osobny obiekt GCC z aktualnym plikiem
    
    public void execute(List<String> output) {
        
        // do zrobienia blokada w momencie w ktorym byla zmiania i nie bylo kompilacji (wtedy ponizszy warunek bedzie do wyrzucenia)
        if(file != null) { // jesli file zostal zainicjalizowany, to znaczy, ze kompilacja tez sie odbyla
            try {
                ProcessBuilder builder = new ProcessBuilder(this.executableName);
                Process p = builder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                
                String line = null;
                while((line = reader.readLine()) != null) {
                    output.add(line);
                }
                p.waitFor();
                p.destroy();
            } catch (IOException ex) {
                Logger.getLogger(GCC.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(GCC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            System.out.println("Brak pliku wykonywalnego");
        }
    }
}
