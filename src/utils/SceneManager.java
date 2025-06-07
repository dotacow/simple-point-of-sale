package utils;

import views.globalScenes.DashBoardScene;
import javafx.application.Platform;
import javafx.stage.Stage;
import models.User;
import views.managersScenes.*;
import views.cashierScenes.*;
import views.globalScenes.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized Scene Manager for efficient navigation and state management
 */
public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;
    private User currentUser;
    private Map<String, Object> sceneCache;
    private String currentScene;
    private boolean wasMaximized;

    private SceneManager() {
        this.sceneCache = new HashMap<>();
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void initialize(Stage stage) {
        this.primaryStage = stage;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    private void saveStageState() {
        if (primaryStage != null) {
            wasMaximized = primaryStage.isMaximized();
        }
    }

    private void restoreStageState() {
        Platform.runLater(() -> {
            if (wasMaximized && primaryStage != null) {
                primaryStage.setMaximized(true);
            }
        });
    }

    public void showLogin() {
        currentScene = "login";
        LoginView loginView = (LoginView) sceneCache.get("login");
        if (loginView == null) {
            loginView = new LoginView(primaryStage);
            sceneCache.put("login", loginView);
        }
        loginView.show();
    }

    public void showDashboard() {
        if (currentUser == null) {
            System.out.println("Error: No user logged in");
            showLogin();
            return;
        }
        if ("dashboard".equals(currentScene)) {
            Object existingScene = sceneCache.get("dashboard");
            if (existingScene instanceof DashBoardScene) {
                ((DashBoardScene) existingScene).refreshData();
            }
            return;
        }

        saveStageState();

        DashBoardScene dashboardScene = (DashBoardScene) sceneCache.get("dashboard");
        if (dashboardScene == null) {
            dashboardScene = new DashBoardScene(primaryStage, currentUser);
            sceneCache.put("dashboard", dashboardScene);
        } else {
            dashboardScene.refreshData();
        }

        dashboardScene.show();
        currentScene = "dashboard";
        restoreStageState();
    }

    public void showManageProducts() {
        if (currentUser == null || !currentUser.isManager()) {
            System.out.println("Access denied: Manager role required");
            return;
        }

        if ("manage_products".equals(currentScene)) {
            return;
        }

        saveStageState();

        ManageProductsScene manageProductsScene = (ManageProductsScene) sceneCache.get("manage_products");
        if (manageProductsScene == null) {
            manageProductsScene = new ManageProductsScene(primaryStage, currentUser);
            sceneCache.put("manage_products", manageProductsScene);
        }

        manageProductsScene.show();
        currentScene = "manage_products";
        restoreStageState();
    }

//todo:
//    public void showMakeSale() {
//        if (currentUser == null) {
//            System.out.println("Error: No user logged in");
//            return;
//        }
//
//        if ("make_sale".equals(currentScene)) {
//            return;
//        }
//
//        saveStageState();
//
//        MakeSaleScene makeSaleScene = (MakeSaleScene) sceneCache.get("make_sale");
//        if (makeSaleScene == null) {
//            makeSaleScene = new MakeSaleScene(primaryStage, currentUser);
//            sceneCache.put("make_sale", makeSaleScene);
//        }
//
//        makeSaleScene.show();
//        currentScene = "make_sale";
//        restoreStageState();
//    }
//  todo:
//    public void showCheckCash() {
//        if (currentUser == null) {
//            System.out.println("Error: No user logged in");
//            return;
//        }
//
//        if ("check_cash".equals(currentScene)) {
//            return;
//        }
//
//        saveStageState();
//
//        CheckCashScene checkCashScene = (CheckCashScene) sceneCache.get("check_cash");
//        if (checkCashScene == null) {
//            checkCashScene = new CheckCashScene(primaryStage, currentUser);
//            sceneCache.put("check_cash", checkCashScene);
//        }
//
//        checkCashScene.show();
//        currentScene = "check_cash";
//        restoreStageState();
//    }
//
    
    public void logout() {
    setCurrentUser(null); // Clear current user and user-specific cache
    currentScene = "login";
    showLogin();
    }
//  todo:
//    public void showManageUsers() {
//    if (currentUser == null || !currentUser.isManager()) {
//        System.out.println("Access denied: Manager role required");
//        return;
//    }
//
//    if ("manage_users".equals(currentScene)) {
//        return;
//    }
//
//    saveStageState();
//
//    ManageUsersScene manageUsersScene = (ManageUsersScene) sceneCache.get("manage_users");
//    if (manageUsersScene == null) {
//        manageUsersScene = new ManageUsersScene(primaryStage, currentUser);
//        sceneCache.put("manage_users", manageUsersScene);
//    }
//
//    manageUsersScene.show();
//    currentScene = "manage_users";
//    restoreStageState();
//}
//    todo:
//    public void showSalesStats() {
//    if (currentUser == null || !currentUser.isManager()) {
//        System.out.println("Access denied: Manager role required");
//        return;
//    }
//
//    if ("sales_stats".equals(currentScene)) {
//        return;
//    }
//
//    saveStageState();
//
//    SalesStatsScene salesStatsScene = (SalesStatsScene) sceneCache.get("sales_stats");
//    if (salesStatsScene == null) {
//        salesStatsScene = new SalesStatsScene(primaryStage, currentUser);
//        sceneCache.put("sales_stats", salesStatsScene);
//    }
//
//    salesStatsScene.show();
//    currentScene = "sales_stats";
//    restoreStageState();
//}
    private void clearUserSpecificCache() {
    sceneCache.remove("dashboard");
    sceneCache.remove("manage_products");
    sceneCache.remove("manage_users");
    sceneCache.remove("sales_stats");
    sceneCache.remove("make_sale");
    sceneCache.remove("check_cash");
}

}
