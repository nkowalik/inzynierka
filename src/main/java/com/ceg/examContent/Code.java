package com.ceg.examContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.fxmisc.richtext.CodeArea;

/**
 *
 * @author marta
 */
public class Code {
    
    public static void saveCode(int idx, CodeArea code) { 
        Task task = Exam.getInstance().getTaskAtIndex(idx);
        task.setTestCode(new ArrayList<>((Arrays.asList(code.getText().split("\n")))));
        
            /* usuwa zamarkowane znaki i dodaje kod do klasy Task */
       
        String newCode = code.getText();
        String newPDFCode = code.getText();   
        
         if(task.getType().name.equals("Gaps"))
             task.getAnswers().clear();
        
        for (int i = code.getText().length() - 1; i >= 0;) {
            if(code.getText(i, i+1).equals("\n")){
                i--;
                continue;
            }
            List<String> s = (List<String>) code.getStyleOfChar(i);
            if (!s.isEmpty()) {
                int j = i-1;
                for(; j >= 0; j--){
                    List<String> styleOfNextChar = (List<String>) code.getStyleOfChar(j);
                    if(styleOfNextChar.isEmpty() || !s.get(s.size() - 1).equals(styleOfNextChar.get(styleOfNextChar.size() - 1))){
                        break;
                    }
                }
                if ("test".equals(s.get(s.size() - 1))) {
                    newCode = newCode.substring(0, j+1) + newCode.substring(i+1);
                    newPDFCode = newPDFCode.substring(0, i) + newPDFCode.substring(i+1);
                }
                if ("hidden".equals(s.get(s.size() - 1))) {
                    newPDFCode = newPDFCode.substring(0, j+1) + newPDFCode.substring(i+1);
                }
                if ("Gaps".equals(task.getType().name) && "gap".equals(s.get(s.size() - 1))) {
                    if(task.getType().name.equals("Gaps"))
                        task.getAnswers().add(0,newPDFCode.substring(j+1, i+1));
                    newPDFCode = newPDFCode.substring(0, j+1) + " #placeForAnswer" + newPDFCode.substring(i+1);                  
                }
                i = j;
            }
            else{
                i--;
            }
        }
        task.setCode(new ArrayList<>(Arrays.asList(newCode.split("\n"))));
        task.setPDFCode(new ArrayList<>(Arrays.asList(newPDFCode.split("\n"))));
        if(task.getType().name.equals("Gaps"))
             task.getType().getParams().setNoOfAnswers(task.getAnswers().size());
    }
    
}
