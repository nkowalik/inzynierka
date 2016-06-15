package com.ceg.gui;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * @author Natalia
 */

public class GUIMain extends Application {
    
    private static Stage pStage;
    
    public static Stage getStage() {
        return pStage;
    }
    
     @Override
    public void start(Stage primaryStage) throws Exception{
        pStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/firstPage.fxml"));
        primaryStage.setTitle("CEG");

        primaryStage.setScene(new Scene(root, 950, 551));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */

}
