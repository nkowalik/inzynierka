package com.ceg.examContent;

import com.ceg.utils.ContentCssClass;

/**
 *
 * @author Martyna
 */
public class ContentPart {
    private final ContentCssClass cssClassName;
    private final String text;
    
    public ContentPart(ContentCssClass fontType, String text) {
        this.cssClassName = fontType;
        this.text = text;
    }
    
    public String getText() {
        return text;
    }
    
    public ContentCssClass getCssClassName() {
        return cssClassName;
    }
}
