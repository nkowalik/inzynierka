package com.ceg.compiler;

import com.ceg.examContent.Exam;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.lang.IllegalThreadStateException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.CodeSource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import org.apache.commons.lang.SystemUtils;

public class GCC {

    private String path;
    private File file = null;
    private String cppName;
    private String executableName;
    public String osName;

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
     * Tworzy nowy plik, ustawia nazwę pliku z kodem oraz pliku wykonywalnego w obiekcie GCC.
     * @param lines Lista linii które mają znaleźć się w pliku.
     * @param name Nazwa pliku do utworzenia.
     * @return Wartość logiczna określająca powodzenie operacji utworzenia pliku.
     */
    public boolean createFile(List<String> lines, String name) {
        if (!lines.isEmpty() && !(lines.get(0).equals("") && lines.size() == 1)) {
            try {
                file = File.createTempFile(name,".cpp" ,new File(path));
                Files.write(file.toPath(), lines, Charset.forName("UTF-8"));

            } catch (IOException ex) {
                Logger.getLogger(GCC.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.cppName = this.file.getAbsolutePath();
            this.executableName = this.file.getAbsolutePath().substring(0, this.file.getAbsolutePath().lastIndexOf("."));
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
                if (SystemUtils.IS_OS_WINDOWS || SystemUtils.IS_OS_LINUX) {                  
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
                reader.close();
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

    
   /* private static Task readOutput(BufferedReader reader, List<String> output) {
        return new Task() {
            @Override protected call() throws Exception {
                String line = null;
                    while((line = reader.readLine()) != null) {
                        output.add(line);
                    }
            }
        };
}
    */
    
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
                    builder.redirectErrorStream(true);
                    Process p = builder.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = null;
                    boolean timeout = false;
                    long elapsedTimeMillis = 0;
                    float elapsedTimeSec = 0;
                    long start = System.currentTimeMillis();
                    float timeoutVal = Exam.getInstance().getExecutionTimetout();
                    while(elapsedTimeSec < timeoutVal ) {                      
                        elapsedTimeMillis = System.currentTimeMillis()-start;
                        elapsedTimeSec = elapsedTimeMillis/1000F;
                        if(reader.ready()){
                            line = reader.readLine();
                            if(line!=null){
                                output.add(line);
                            }
                            else{
                                 break;
                            }
                        }

                    }
                    p.waitFor(1,TimeUnit.SECONDS);
                    try{
                       if(p.exitValue() != 0){
                           output.add("Błąd wykonania.");                    
                       } 
                    }
                    catch(IllegalThreadStateException ex){
                        output.clear();
                        output.add("Kompilacja przebiegła pomyślnie.");
                        output.add("Upłynięcie limitu czasu wykonania."); 
                    }                 
                    p.destroy();
                    reader.close();
                    String tmpFileName = null;
                    if(SystemUtils.IS_OS_WINDOWS)
                        tmpFileName = this.executableName + ".exe";
                    else if(SystemUtils.IS_OS_LINUX)
                        tmpFileName = this.executableName;
                    File tmp = new File(tmpFileName);
                    if(tmp.exists()){
                        tmp.delete();
                    }
                    if(file.exists()){
                        if(file.delete()){
                           file = null; 
                        }
                        else{
                            file.deleteOnExit();
                        }
                    }
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
