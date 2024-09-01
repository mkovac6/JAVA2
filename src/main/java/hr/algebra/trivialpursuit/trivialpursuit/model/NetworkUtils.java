package hr.algebra.trivialpursuit.trivialpursuit.model;

import hr.algebra.trivialpursuit.trivialpursuit.GameState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkUtils {
    private ObjectOutputStream output;
    private ObjectInputStream input;

    private static NetworkUtils instance = new NetworkUtils();

    private NetworkUtils() {

    }

    public static NetworkUtils getInstance() {
        return instance;
    }

    public void connectToServerAndSendMessage() {
        try {
            // Connect to the server
            Socket socket = new Socket("localhost", 12345);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            // Send a hardcoded message to the server
            sendMessage("Hello from the client!");

            // Optionally, you can listen for responses from the server
            listenForMessages();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenForMessages() {
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

    public void sendMessage(String content) {
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

    public void sendGameState(GameState gameState) {
        System.out.println("Sent to server: " + gameState);
        if (output != null) {
            try {
                output.writeObject(gameState);
                output.flush();
                System.out.println("Sent to server: " + gameState);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
