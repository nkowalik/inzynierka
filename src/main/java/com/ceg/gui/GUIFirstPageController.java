package com.ceg.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;


/**
 *
 * @author Natalia
 */

public class GUIFirstPageController {

    public void createNewExam(ActionEvent event) throws Exception {
        Parent firstPage = FXMLLoader.load(getClass().getResource("/fxml/secondPage.fxml"));
        Scene firstScene = new Scene (firstPage);

        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.hide();
        appStage.setScene(firstScene);
        appStage.show();
    }
}
