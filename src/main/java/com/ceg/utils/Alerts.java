package com.ceg.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

public final class Alerts {

    /**
     * Wyświetla okno z informacją o braku części zadania.
     */
    public static void emptyPartOfTaskAlert() {
        showAlert(AlertType.INFORMATION, "Uwaga!", "Brak części zadania.",
                "Sprawdź czy pola z poleceniem i kodem nie są puste \n"
                + "oraz czy podana liczba odpowiedzi zgadza się ze stanem faktycznym.");
    }

    /**
     * Wyświetla okno z błędem dot. nieprawidłowego parsowania kodu.
     */
    public static void parsingCodeErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd podczas parsowania kodu.", "Sprawdź poprawność kodu.");
    }

    /**
     * Wyświetla okno z błędem dot. nieprawidłowego wygenerowania odpowiedzi.
     */
    public static void generatingAnswersErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd podczas generowania odpowiedzi.", "Sprawdź poprawność kodu.");
    }

    /**
     * Wyświetla okno z błędem dot. generowania pdf'a dla pustego arkusza.
     */
    public static void emptyExamAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Egzamin jest pusty", "Nie można wygenerować pustego arkusza, dodaj zadania.");
    }
    
    /**
    * Wyświetla okno z informacją o błędzie kompilacji podczas automatyczbej kompilacji egzaminu
    */
    public static void compileErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd kompilacji", "Popraw błędy w kodzie dolączonym do zadania.");
    }

    /**
     * Wyświetla okno z alertem.
     * @param alertType Typ alertu.
     * @param title Tytuł okna.
     * @param headerText Tekst nagłówka okna.
     * @param contextText Wiadomość zawarta w oknie.
     */
    private static void showAlert(AlertType alertType, String title, String headerText, String contextText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
               
        alert.showAndWait();
    }
}
