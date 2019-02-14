package main.loginClient;

import javafx.application.Application;
import javafx.scene.Scene;
//import javafx.application.*;
import javafx.scene.web.*;
import javafx.stage.Stage;

public class LoginClient extends Application {    
    private static String url = "";
    
    public static void main(String[] args) {
        launch(args);
       
       
    }
    
    @Override public void start(Stage stage) throws Exception {
        
        
        WebView webview = new WebView();
        webview.getEngine().load(url);
        webview.setPrefSize(1000, 800);

        Scene scene = new Scene(webview);
        stage = new Stage();
        stage.setScene(scene);
        stage.show();
        
    }

}