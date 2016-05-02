package com.ceg.pdf;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/* klasa odpowiedzialna za generowanie pdfa. Aby utworzyć dokument należy utworzyć obiekt tej klasy za pomocą 
   konstruktora, który jako argumenty przyjmuje nazwę przyszłego pliku pdf, tekst polecenia i listę linii kodu */
public class PDFGenerator {
    private final PDDocument document;
    private PDPage actualPage;
    public static PDPageContentStream cs;
    private final int leftMargin = 30;
    private int topMargin = 760;
    private final int breakBetweenTasks = 45;
    
    /* wywołanie konstruktora powoduje utworzenie dokumentu pdf. Argumentami nazwa przyszłego pliku pdf
       oraz String z poleceniem i lista linii kodu */
    public PDFGenerator(String fileName, String command, List<String> code) throws IOException {
        
        //tworzenie dokumentu
        document = new PDDocument();
        
        //tworzenie nowej strony i dodanie jej do dokumentu (na razie zakłada się, że jest jedna strona
        actualPage = new PDPage();
        document.addPage(actualPage);
        cs = new PDPageContentStream(document, actualPage);
        
        setNameLastNameAndStudentNumber();
        
        //dodanie określonej przerwy pomiędzy zadaniami i pomiędzy nagłówkiem a pierwszym zadaniem
        topMargin -= breakBetweenTasks;

        //utworzenie obiektu polecenia i wywołanie na nim metody formatowania i zapisania do pdf
        PDFCommand comm = new PDFCommand(leftMargin, topMargin, 250);
        comm.textSplitting(command);
        
        //utworzenie obiektu kodu i wywołanie na nim metody formatowania i zapisania do pdf
        PDFCode cod = new PDFCode(250 + leftMargin + 1, topMargin, 250);
        cod.textSplitting(code);

        cs.close();
        savePDF(fileName);
    }
    
    /* Funkcja dodająca nagłówek postaci miejsc na imię  i nazwisko oraz numer indeksu studenta */
    private void setNameLastNameAndStudentNumber() throws IOException {        
        writeLine(  "_________________________________________________________", 
                    leftMargin, topMargin);
        writeLine("___________", 500, topMargin);
        
        //przejście do następnej linii
        topMargin -= 15;
        
        writeLine("Imie i nazwisko", leftMargin + 1, topMargin);
        writeLine("Nr indeksu", 501, topMargin);
    }
    
    /* Funkcja tworząca dokument pdf */
    private void savePDF(String fileName) throws IOException {
        File newFile = new File(fileName);
        document.save(newFile);
        document.close();
    }
    
    /* Funkcja do zapisu linii do dokumentu. Potrzebna do zapisania nagłówka. Wykorzystywany font: 
       Times New Roman o wielkości 12 */
    public void writeLine(String text, int startX, int startY) throws IOException {
        PDFont font = PDType1Font.TIMES_ROMAN;       
        cs.setFont(font, 12);
        
        cs.beginText();
        cs.newLineAtOffset(startX, startY);
        cs.showText(text);
        cs.endText();
    }
}
