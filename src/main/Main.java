package main;
import controllers.UserController;
import javafx.application.Application;
import javafx.stage.Stage;
import models.User;

import views.globalScenes.*;
import views.managersScenes.ManageProductsScene;
import utils.SceneManager;
import views.managersScenes.ManageUsersScene;
/**
 * @author dotacow  
 */
public class Main extends Application {   
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("abu m7md's supermarket");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        String username = "1";
        String password = "1";
        User authenticatedUser = UserController.authenticate(username, password);
        ManageUsersScene manageUsersScene = new ManageUsersScene();
        manageUsersScene.start(primaryStage, authenticatedUser);
        
    }
  
    public static void main(String[] args) {
        launch(args);
    }
}