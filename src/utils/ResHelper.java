/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;


/**
 *
 * @author dotac
 */
public class ResHelper{
    
static Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
 public static double getWidth()
 {
     return 1920;
//     return screenBounds.getWidth();
 }
 public static double getHeight()
 {
     return 1080;
//     return screenBounds.getHeight();
 }
}
