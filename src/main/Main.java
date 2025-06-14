package main;

import javafx.application.Application;
import javafx.stage.Stage;
import views.globalScenes.*;
/**
 *
 * @author dotacow  
 */
public class Main extends Application
{  
        @Override
    public void start(Stage primaryStage)
    {
//        primaryStage.setMinHeight(ResHelper.getHeight());
//        primaryStage.setMinWidth(ResHelper.getWidth());
//        primaryStage.setWidth(800);
//        primaryStage.setHeight(500);
        new LoginView(primaryStage).show();   
    }
  
    public static void main(String[] args) {
        launch(args);
    }
}
