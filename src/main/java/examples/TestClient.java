package examples;

import backend.client.ChatClient;
import controller.LoginController;
import java.net.UnknownHostException;
import ui.LoginUI;

public class TestClient {
    public static final String serverIPAddress = "192.168.1.2";
    public static void main(String[] args) throws UnknownHostException{
        LoginUI loginUI = new LoginUI();
        ChatClient chatClient = new ChatClient(serverIPAddress, 11111);
        LoginController loginController = new LoginController(loginUI, chatClient);
        loginController.initController();
    }
}
