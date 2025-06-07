package main;

import javafx.application.Application;
import javafx.stage.Stage;

import views.globalScenes.*;
import views.managersScenes.ManageProductsScene;
/**
 *
 * @author dotacow  
 */
public class Main extends Application
{   
    @Override
    public void start(Stage primaryStage)
    {
        new LoginView(primaryStage).show();
    }
  
    public static void main(String[] args) {
        launch(args);
    }
}
