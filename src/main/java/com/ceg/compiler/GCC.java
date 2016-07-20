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
    private File file = null;
    private String cppName;
    private String executableName;
    public String osName;

    public GCC() {
        osName = System.getProperty("os.name").toLowerCase();

        CodeSource codeSource = GCC.class.getProtectionDomain().getCodeSource();
        File jarFile;
        try {
            jarFile = new File(codeSource.getLocation().toURI().getPath());
            path = jarFile.getParentFile().getPath();
        } catch (URISyntaxException ex) {
            Logger.getLogger(GCC.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean createFile(List<String> lines, String name) {
        if (!lines.isEmpty() && !(lines.get(0).equals("") && lines.size() == 1)) {
            try {
                Files.write(Paths.get(this.path + "/" + name), lines, Charset.forName("UTF-8"));
                file = new File(this.path + "/" + name);

            } catch (IOException ex) {
                Logger.getLogger(GCC.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.cppName = this.file.getAbsolutePath();
            this.executableName = this.path.toString() + "/" + this.file.getName().substring(0, this.file.getName().lastIndexOf("."));
            return true;
        } else {
            return false;
        }
    }
    public boolean compile(List<String> output) {
        if (file.exists()) {
            try {
                ProcessBuilder builder = null;
                if (SystemUtils.IS_OS_WINDOWS) {
                    builder = new ProcessBuilder(new String[]{"cmd.exe", "/c", "g++", "-o", this.executableName, this.cppName});
                } else if (SystemUtils.IS_OS_LINUX) {
                    builder = new ProcessBuilder(new String[]{"g++", "-o", this.executableName, this.cppName});
                } else {
                    System.out.println("Nieobsługiwany system operacyjny");
                    return false;
                }
                Process p = builder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    output.add(line);
                }
                p.waitFor();
                p.destroy();
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
    public void execute(List<String> lines, String name, List<String> output) {
        if(createFile(lines, name)) {
            if(compile(output)) {
                output.add("Kompilacja przebiegła pomyślnie.");
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
            } // jeśli kompilacja się nie powiedzie, output i tak zostanie już zapisany
        }
        else {
            output.add("Błąd. Pusty plik wejściowy.");
        }
    }
}
