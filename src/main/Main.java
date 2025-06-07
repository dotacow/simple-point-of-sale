
package main;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import views.managersScenes.*;
import views.globalScenes.*;
import views.cashierScenes.*;
/**
 *
 * @author dotacow
 */
public class Main extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        new ManageProductsScene().start(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
