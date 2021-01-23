package controller;

import backend.client.ChatClient;
import backend.server.ChatServer;
import backend.server.ClientHandler;
import ui.AddFriendUI;
import ui.MainUI;

import javax.print.MultiDocPrintService;
import javax.swing.*;

public class AddFriendController {
    AddFriendUI addFriendUI;
    ChatClient chatClient;
    private boolean check_valid;

    public AddFriendController(AddFriendUI addFriendUI, ChatClient chatClient) {
        this.addFriendUI = addFriendUI;
        this.chatClient = chatClient;
        this.check_valid = false;
    }

    public void initController() {
        addFriendUI.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addFriendUI.setVisible(true);
        this.addFriendUI.getAdd_but().addActionListener(e -> addFriend());

    }
    private void addFriend() {
        String friendName = this.addFriendUI.getUserInput();
        if (friendName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Inavlid input!");
        } else {
            this.check_valid = true;
            this.chatClient.sendReq("addfriend-" + friendName);
            System.out.println("addfriend-" + friendName);
        }
        if (check_valid) {
            String resMess = null;
            do {
                synchronized (this) {
                    resMess = this.chatClient.getResponseMessage();
                }
            } while (resMess == null);
            if (resMess.equals("addfriend-success")) {
                // add later
                this.addFriendUI.setVisible(false);
            }
            this.chatClient.setResponseMessage(null);
        }
        check_valid = false;
    }
}

 