package hr.algebra.trivialpursuit.trivialpursuit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class HelloApplication extends Application {
    private ObjectOutputStream output;
    private ObjectInputStream input;

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML view
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        stage.setTitle("Trivial Pursuit!");
        stage.setScene(scene);
        stage.show();

        // Connect to the server and send a message
        connectToServerAndSendMessage();
    }

    private void connectToServerAndSendMessage() {
        try {
            // Connect to the server
            Socket socket = new Socket("localhost", 12345);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            // Send a hardcoded message to the server
            sendMessage("Hello from the client!");

            // Optionally, you can listen for responses from the server
            listenForMessages();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenForMessages() {
        new Thread(() -> {
            try {
                Object obj;
                while ((obj = input.readObject()) != null) {
                    System.out.println("Received from server: " + obj.toString());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMessage(String content) {
        if (output != null) {
            try {
                // Just sending the String directly, no need for a Message object
                output.writeObject(content);
                output.flush();
                System.out.println("Sent to server: " + content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch();
    }
}
