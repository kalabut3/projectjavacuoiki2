package client;

import java.io.*;
import java.net.Socket;

public class ClientConnection {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientConnection() {
        try {
            socket = new Socket("localhost", 9999);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String text) {
        out.println(text);
    }

    public String receive() {
        try {
            return in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
