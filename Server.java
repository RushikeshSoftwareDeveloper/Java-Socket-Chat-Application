import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

public class Server {

    private static Set<ClientHandler> clients = new HashSet<>();

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server Started...");

        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler client = new ClientHandler(socket);
            clients.add(client);
            new Thread(client).start();
        }
    }

    static class ClientHandler implements Runnable {

        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Enter Username:");
                username = in.readLine();

                String message;

                while ((message = in.readLine()) != null) {

                    saveMessage(username, message);

                    for (ClientHandler client : clients) {
                        client.out.println(username + ": " + message);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void saveMessage(String user, String msg) {
            try (Connection con = DBConnection.getConnection()) {
                String query = "INSERT INTO messages(username,message) VALUES(?,?)";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, user);
                ps.setString(2, msg);
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
