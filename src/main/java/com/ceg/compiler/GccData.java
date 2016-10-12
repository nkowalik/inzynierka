/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceg.compiler;

/**
 *
 * @author Marta
 */
public class GccData {
    
    private String cppName;
    private String executableName;
    private GCC compilerInstance;
 
    public String getCppName() {
        return cppName;
    }

    public void setCppName(String cppName) {
        this.cppName = cppName;
    }

    public String getExecutableName() {
        return executableName;
    }

    public void setExecutableName(String executableName) {
        this.executableName = executableName;
    }

    public GCC getCompilerInstance() {
        return compilerInstance;
    }

    public void setCompilerInstance(GCC compilerInstance) {
        this.compilerInstance = compilerInstance;
    }
    
    
}
