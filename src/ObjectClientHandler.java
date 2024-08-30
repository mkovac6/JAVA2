import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

class ObjectClientHandler extends Thread {
    private final Socket socket;
    private static final List<ObjectClientHandler> clients = new CopyOnWriteArrayList<>();
    private static AtomicInteger playerCount = new AtomicInteger(0);
    private String playerName;
    private ObjectOutputStream out;

    public ObjectClientHandler(Socket socket) {
        this.socket = socket;
        this.playerName = "Player " + playerCount.incrementAndGet(); // Assign player name
    }

    public void run() {
        /*(ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream()))*/
        try {

            // Send welcome message to the client
            out = new ObjectOutputStream(socket.getOutputStream());
            clients.add(this);

            broadcastMessage(playerName + " has joined the chat!");

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            Object obj;
            while ((obj = input.readObject()) != null) {
                System.out.println(playerName + " sent: " + obj);
                broadcastMessage(playerName + " says: " + obj);
                out.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    clients.remove(this);
                    broadcastMessage(playerName + " has left the chat!");
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void broadcastMessage(String message) {
        for (ObjectClientHandler client : clients) {
            try {
                client.out.writeObject(message);
                client.out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}