package main;
import javafx.application.Application;
import javafx.stage.Stage;
import views.globalScenes.*;

/**
 * @author dotacow  
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("abu m7md's supermarket");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // Create and show login view
        LoginView loginView = new LoginView(primaryStage);
        loginView.show();

        // primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}