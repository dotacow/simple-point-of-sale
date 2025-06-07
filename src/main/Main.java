package main;
import javafx.application.Application;
import javafx.stage.Stage;

import views.globalScenes.*;
import views.managersScenes.ManageProductsScene;
import utils.SceneManager;
/**
 * @author dotacow  
 */
public class Main extends Application {   
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("abu m7md's supermarket");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // Initialize the SceneManager with the primary stage
        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.initialize(primaryStage);
        // Show login screen through SceneManager
        sceneManager.showLogin();
    }
  
    public static void main(String[] args) {
        launch(args);
    }
}