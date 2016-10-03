/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.compiler;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marta
 */
public class GccWin extends GCC{
    private Process console;
    private ProcessBuilder builder = null;
    
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
    
}
