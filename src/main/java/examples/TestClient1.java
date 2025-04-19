package examples;

import backend.client.ChatClient;
import controller.LoginController;
import ui.LoginUI;

import java.net.UnknownHostException;

public class TestClient1 {
    public static final String serverIPAddress = "172.16.0.204";
    public static void main(String[] args) throws UnknownHostException {
        LoginUI loginUI = new LoginUI();

        ChatClient chatClient = new ChatClient(serverIPAddress, 11111);
        LoginController loginController = new LoginController(loginUI, chatClient);
        loginController.initController();
    }
}
