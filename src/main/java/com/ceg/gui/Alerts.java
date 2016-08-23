package com.ceg.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author Martyna
 */
public final class Alerts {
    public static void emptyPartOfTaskAlert() {
        showAlert(AlertType.INFORMATION, "Uwaga!", "Brak części zadania.",
                "Sprawdź czy pola z poleceniem i kodem nie są puste \n"
                + "oraz czy podana liczba odpowiedzi zgadza się ze stanem faktycznym.");
    }
    
    public static void parsingCodeErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd podczas parsowania kodu.", "Sprawdź poprawność kodu.");
    }
    
    public static void generatingAnswersErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd podczas generowania odpowiedzi.", "Sprawdź poprawność kodu.");
    }
    
    public static void emptyExamAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Egzamin jest pusty", "Nie można wygenerować pustego arkusza, dodaj zadania");
    }
    
    private static void showAlert(AlertType alertType, String title, String headerText, String contextText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
               
        alert.showAndWait();
    }
}
