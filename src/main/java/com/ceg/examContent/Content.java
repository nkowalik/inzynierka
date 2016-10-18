package com.ceg.examContent;

import com.ceg.utils.FontTypeUtil;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import org.fxmisc.richtext.StyleClassedTextArea;

/**
 *
 * @author Martyna
 */

public class Content {
    private List<ContentPart> contentParts;
    
    public Content() {
        contentParts = new ArrayList<>();
    }
    
    public void extractContent(StyleClassedTextArea text) {
        contentParts.clear();
        int length = text.getLength();
        String currentStyle = "[]";
        StringBuilder sb = new StringBuilder();
        //String withoutEndOfLine = text.getText().replaceAll("\n", " ");

        if(text.getText().contains("\n")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Niezaimplementowana funkcjonalność!");
            alert.setHeaderText(null);
            alert.setContentText("Uwaga, znak nowej linii w poleceniu nie jest jeszcze obsłużony");

            alert.showAndWait();
        }

        for(int i = 0; i < length; i++) {
            if(text.getStyleOfChar(i).toString().equals(currentStyle)) {
                sb.append(text.getText().charAt(i));
            }
            else {
                if(i != 0) {
                    ContentPart part = new ContentPart(FontTypeUtil.stringToContentCssClass(currentStyle), sb.toString());
                    sb.setLength(0);
                    contentParts.add(part);
                }
                sb.append(text.getText().charAt(i));
                currentStyle = text.getStyleOfChar(i).toString();
            }
        }
        ContentPart part = new ContentPart(FontTypeUtil.stringToContentCssClass(currentStyle), sb.toString());
        sb.setLength(0);
        contentParts.add(part);
    }
    
    public void creatStyleClassedTextAreaText(StyleClassedTextArea textArea) {
        textArea.clear();
        StringBuilder sb = new StringBuilder();
        contentParts.stream().forEach((part) -> {
            sb.append(part.getText());
        });
        textArea.appendText(sb.toString());
        int start;
        start = 0;
        for(ContentPart part : contentParts) {
            textArea.setStyleClass(start, start + part.getText().length(), part.getCssClassName().getClassName());
            start += part.getText().length();
        }
    }
    
    public List<ContentPart> getContentParts() {
        return contentParts;
    }
    
    public void setContentParts(List<ContentPart> contentParts) {
        this.contentParts = contentParts;
    }
}