package server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(9999);
            System.out.println("Server đang chạy...");
            while (true) {
                Socket socket = server.accept();
                System.out.println("Client đã kết nối");
                ClientHandler handler = new ClientHandler(socket);
                handler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
