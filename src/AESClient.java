import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;


public class AESClient extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AES加密算法");

        WebView aesWebView = new WebView();
        WebEngine aesEngine = aesWebView.getEngine();
        aesEngine.load(getClass().getResource("aes.html").toExternalForm());
        StackPane aesRoot = new StackPane(aesWebView);
        Scene aesScene = new Scene(aesRoot, 800, 600);

        WebView attackWebView = new WebView();
        WebEngine attackEngine = attackWebView.getEngine();
        attackEngine.load(getClass().getResource("attack.html").toExternalForm());
        StackPane attackRoot = new StackPane(attackWebView);
        Scene attackScene = new Scene(attackRoot, 800, 600);

        WebView CBCWebView = new WebView();
        WebEngine CBCEngine = CBCWebView.getEngine();
        CBCEngine.load(getClass().getResource("CBC.html").toExternalForm());
        StackPane CBCRoot = new StackPane(CBCWebView);
        Scene CBCScene = new Scene(CBCRoot, 800, 600);

        primaryStage.setScene(aesScene);
        primaryStage.show();

        AES aes = new AES(primaryStage, aesScene, attackScene, CBCScene);

        aesEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) aesEngine.executeScript("window");
                window.setMember("java", aes);
            }
        });

        attackEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) attackEngine.executeScript("window");
                window.setMember("java", aes);
            }
        });

        CBCEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) CBCEngine.executeScript("window");
                window.setMember("java", aes);
            }
        });

    }
}

