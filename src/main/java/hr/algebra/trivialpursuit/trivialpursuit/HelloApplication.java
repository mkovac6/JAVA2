package hr.algebra.trivialpursuit.trivialpursuit;

import hr.algebra.trivialpursuit.trivialpursuit.model.NetworkUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private NetworkUtils networkUtils;

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML view
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        stage.setTitle("Trivial Pursuit!");
        stage.setScene(scene);
        stage.show();


        // Connect to the server and send a message
        networkUtils = NetworkUtils.getInstance();
        networkUtils.connectToServerAndSendMessage();
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch();
    }
}
