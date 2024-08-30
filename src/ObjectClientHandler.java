import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

class ObjectClientHandler extends Thread {
    private final Socket socket;
    private static AtomicInteger playerCount = new AtomicInteger(0);
    private String playerName;

    public ObjectClientHandler(Socket socket) {
        this.socket = socket;
        this.playerName = "Player " + playerCount.incrementAndGet(); // Assign player name
    }

    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {

            // Send welcome message to the client
            output.writeObject("Welcome " + playerName + "!");
            output.flush();

            Object obj;
            while ((obj = input.readObject()) != null) {
                System.out.println(playerName + " sent: " + obj);
                output.writeObject("Echo from " + playerName + ": " + obj); // Echo the object back
                output.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}