import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost", 5000);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);

        Scanner sc = new Scanner(System.in);

        new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    System.out.println(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        while (true) {
            String msg = sc.nextLine();
            out.println(msg);
        }
    }
}
