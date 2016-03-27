/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testgenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 *
 * @author Pawel
 */
public class Ccompiler {
    
    public void CompileNRun(String filename){

        try {
            Process cmd = new ProcessBuilder("cmd.exe").start(); // start cmd process
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(cmd.getOutputStream()));
            int version = 10; // requires at least VS 2010
            String path = "C:\\Program Files (x86)\\Microsoft Visual Studio " + version + ".0\\VC\\bin";
            File file = new File(path);
            while(!file.isDirectory()) { // check if directory exists
                if(version < 17) {
                    path = "C:\\Program Files (x86)\\Microsoft Visual Studio " + ++version + ".0\\VC\\bin"; // if not, change the VS version and check again
                    file = new File(path);
                }
                else { // if version >= 17 (does it even exist?)
                    System.out.println("Microsoft Visual Studio compiler not found."); // TODO: provide path to compiler and vcvars32
                    return;
                }
            }
            // TODO: check if files exist
            writer.write("\"" + path + "\\vcvars32\""); // load local variables to console process (needed for proper libraries compilation)
            writer.newLine(); // *enter*
            writer.write("\"" + path + "\\cl\" " + filename); // compile file using VS cl compiler
            writer.newLine();
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(cmd.getInputStream()));
            writer.flush(); 
            line = reader.readLine();
            while (true) {
                if(line.equals(filename.substring(0, filename.indexOf(".")+1) + "obj ")) // name with .obj is the last compiler returned string - after detection stop reading input
                    break;
                line = reader.readLine();
            }
            cmd.destroy(); // kill process, job is done
        }
        catch (Exception err) {
            err.printStackTrace();
        }
        
        return;
        
    }
    
}
