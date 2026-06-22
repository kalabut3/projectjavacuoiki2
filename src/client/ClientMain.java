package client;

public class ClientMain {

    public static void main(String[] args) {
        ClientConnection client = new ClientConnection();
        client.send("Xin chào Server");
        System.out.println(client.receive());
    }
}
