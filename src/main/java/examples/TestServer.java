package examples;

import backend.server.ChatServer;

import java.io.IOException;

public class TestServer {
    public static final String serverIPAddress = "192.168.91.179";

    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer(11111, 30);
        chatServer.start();


    }
}
