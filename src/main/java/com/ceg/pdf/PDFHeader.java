package com.ceg.pdf;

import com.ceg.examContent.Exam;
import com.ceg.utils.ColorPicker;
import com.ceg.utils.ContentCssClass;
import com.ceg.utils.FontType;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import static com.ceg.utils.FontType.SERIF_REGULAR;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;


public class PDFHeader {
    private final FontType fontType = SERIF_REGULAR;
    private final int fontSize = 12;
    private final int placeForIndexNumberX = 400;
    private final int placeForDateX = 501;
    private static int topMargin = PDFSettings.getInstance().topMargin;
    private static final int leftMargin = PDFSettings.getInstance().leftMargin;
    private static final String testDate = PDFSettings.getInstance().getDate();
    protected int titleFontSize;
    protected FontType titleFontType;
    protected ColorPicker titleColor;
    protected int commentFontSize;
    protected FontType commentFontType;
    protected ColorPicker commentColor;
    
        /* Funkcja dodająca nagłówek postaci miejsc na imię  i nazwisko oraz numer indeksu studenta */
    public int setHeader(int breakAfterHeader) throws IOException {
        int top = topMargin;
        PDFLine line;
        PDFLinePart linePart;
        titleFontSize = PDFSettings.getInstance().getTitleFontSize();
        titleFontType = PDFSettings.getInstance().getTitleFont();
        titleColor = PDFSettings.getInstance().getTitleColor();
        commentFontSize = PDFSettings.getInstance().getCommentFontSize();
        commentFontType = PDFSettings.getInstance().getCommentFont();
        commentColor = PDFSettings.getInstance().getCommentColor();
        setFontTypeBoldOrItalic();

        if (!Exam.title.isEmpty()) {
            linePart = new PDFLinePart(titleFontType);
            line = new PDFLine(titleFontSize, leftMargin);
            linePart.setText(Exam.title);
            line.setLineParts(Arrays.asList(linePart));
            line.writeLineInColor(top, titleColor.getColor());
            top -= 30;
        }

        line = new PDFLine(fontSize, leftMargin);
        linePart = new PDFLinePart(fontType);
        linePart.setText("________________________________________________________");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);
       
        line = new PDFLine(fontSize, placeForIndexNumberX);
        linePart = new PDFLinePart(fontType);
        linePart.setText("___________");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);

        line = new PDFLine(fontSize, placeForDateX);
        linePart = new PDFLinePart(fontType);
        linePart.setText(testDate);
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);

        //przejście do następnej linii
        top -= 15;
        
        line = new PDFLine(fontSize, leftMargin + 1);
        linePart = new PDFLinePart(fontType);
        linePart.setText("Imię i nazwisko");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);

        line = new PDFLine(fontSize, placeForIndexNumberX + 1);
        linePart = new PDFLinePart(fontType);
        linePart.setText("Nr indeksu");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);

        if (!Exam.comment.isEmpty()) {
            top -= 30;
            linePart = new PDFLinePart(commentFontType);
            line = new PDFLine(commentFontSize, leftMargin);
            linePart.setText(Exam.comment);
            line.setLineParts(Arrays.asList(linePart));
            line.writeLineInColor(top, commentColor.getColor());
        }

        return top-breakAfterHeader;
    }

    private void setFontTypeBoldOrItalic() {
        if (PDFSettings.getInstance().getIsTitleBold() && PDFSettings.getInstance().getIsTitleItalic()) {
            titleFontType = titleFontType.contentCssClassToFontType(ContentCssClass.BOLD_ITALIC);
        }
        else if (PDFSettings.getInstance().getIsTitleBold()) {
            titleFontType = titleFontType.contentCssClassToFontType(ContentCssClass.BOLD);
        }
        else if (PDFSettings.getInstance().getIsTitleItalic()) {
            titleFontType = titleFontType.contentCssClassToFontType(ContentCssClass.ITALIC);
        }

        if (PDFSettings.getInstance().getIsCommentBold() && PDFSettings.getInstance().getIsCommentItalic()) {
            commentFontType = commentFontType.contentCssClassToFontType(ContentCssClass.BOLD_ITALIC);
        }
        else if (PDFSettings.getInstance().getIsCommentBold()) {
            commentFontType = commentFontType.contentCssClassToFontType(ContentCssClass.BOLD);
        }
        else if (PDFSettings.getInstance().getIsCommentItalic()) {
            commentFontType = commentFontType.contentCssClassToFontType(ContentCssClass.ITALIC);
        }
    }
}
