package hr.algebra.trivialpursuit.trivialpursuit.utils;

import hr.algebra.trivialpursuit.trivialpursuit.GameState;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

public class NetworkUtils {
    private ObjectOutputStream output;
    private ObjectInputStream input;
    public static int SERVER_PORT;

    private static volatile NetworkUtils instance = new NetworkUtils();

    private NetworkUtils() {

    }

    public static NetworkUtils getInstance() {
        synchronized (NetworkUtils.class) {
            if (instance == null) {
                instance = new NetworkUtils();
            }
        }
        return instance;
    }

    static {
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("socket.properties"));
            SERVER_PORT = Integer.parseInt(prop.getProperty("SERVER_PORT"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToServerAndSendMessage() {
        try {

            Socket socket = new Socket("localhost", SERVER_PORT);
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
