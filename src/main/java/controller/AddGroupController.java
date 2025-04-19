package controller;

import backend.client.ChatClient;
import backend.model.Group;
import com.application.chatboxp2p.staticdata.Friend;
import ui.AddGroupUI;
import ui.MainUI;

import javax.swing.*;

public class AddGroupController {
    private AddGroupUI addGroupUI;
    private ChatClient chatClient;
    private MainUI mainUI; // Add mainUI as a field
    private Group group;

    public AddGroupController(AddGroupUI addGroupUI, ChatClient chatClient, MainUI mainUI) {
        this.addGroupUI = addGroupUI;
        this.chatClient = chatClient;
        this.mainUI = mainUI; // Initialize mainUI
        initController();
    }

    public void initController() {
        addGroupUI.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addGroupUI.setVisible(true);

        addGroupUI.getAddUserButton().addActionListener(e -> addUser());
        addGroupUI.getCreateGroupButton().addActionListener(e -> createGroup());
    }

    private void addUser() {
        String userName = addGroupUI.getUserNameInput();
        if (userName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "User name cannot be empty!");
        } else {
            addGroupUI.addUserToList(userName);
        }
    }

    private void createGroup() {
        String groupName = addGroupUI.getGroupNameInput();
        if (groupName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Group name cannot be empty!");
        } else {
            group = new Group(groupName);
            for (String user : addGroupUI.getUserList()) {
                group.addUser(user);
            }
            mainUI.getLf().addUser(new Friend(group.getName(), "group")); // Use mainUI
            mainUI.getList_user().updateUI(); // Use mainUI
            JOptionPane.showMessageDialog(null, "Group '" + group.getName() + "' created successfully with " + group.getUsers().size() + " users.");
            addGroupUI.dispose();
        }
    }
}