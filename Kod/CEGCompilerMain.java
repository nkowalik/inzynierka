/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testgenerator;

/**
 *
 * @author Pawel
 */
public class TestGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        Ccompiler compiler = new Ccompiler(); 
        String filename = "test.cpp";
        compiler.CompileNRun(filename); // put .cpp file in program directory! (TODO: allow random location)
    }
    
}
