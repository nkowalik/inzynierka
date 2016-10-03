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

public class GCC {

    private String path;
    private File file = null;
    public String osName;
    private String cppName;
    private String executableName;
    private ProcessBuilder console;
    private final static GCC instance = new GCC();
    
    public static GCC getInstance() {
        return instance;
    }

    /**
     * Tworzy obiekt kompilatora.
     * Przypisuje nazwę systemu operacyjnego, oraz ścieżkę programu.
     */
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
    
    /**
     * Tworzy proces konsoli
     * 
     * @return true jeśli operacja się powiedzie, false w przypadku błędu
     */
    public boolean startConsole(){
        return true;
    }
    
    public boolean stopConsole(){
        return true;
    }

    /**
     * Tworzy nowy plik, ustawia nazwę pliku z kodem oraz pliku wykonywalnego w obiekcie GCC.
     * @param lines Lista linii które mają znaleźć się w pliku.
     * @param name Nazwa pliku do utworzenia.
     * @return Wartość logiczna określająca powodzenie operacji utworzenia pliku.
     */
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

    /**
     * Wykonuje kompilację pliku, którego nazwa zapisana jest w GCC.
     * @param output Lista linii wygenerowanych przez kompilator.
     * @return Wartość logiczna określająca powodzenie operacji kompilacji pliku.
     */
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

    /**
     * Tworzy, kompiluje i uruchamia plik wykonywalny.
     * @param lines Lista linii które mają znaleźć się w pliku.
     * @param name Nazwa pliku do utworzenia.
     * @param output Lista linii wygenerowanych podczas kompilacji lub wykonania programu.
     */
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
            }
        }
        else {
            output.add("Błąd. Pusty plik wejściowy.");
        }
    }
}
