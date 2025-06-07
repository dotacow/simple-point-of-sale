package main;

import javafx.application.Application;
import javafx.stage.Stage;
import views.managersScenes.ManageProductsScene;

/**
 *
 * @author dotacow
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            new ManageProductsScene().start(primaryStage);
        } catch (Exception e) {
            System.err.println("Error in intilazation: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
