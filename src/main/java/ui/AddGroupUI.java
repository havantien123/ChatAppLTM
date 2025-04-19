package ui;

import backend.model.Group;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddGroupUI extends javax.swing.JFrame {

    private JTextField groupNameField;
    private JTextField userNameField;
    private JButton addUserButton;
    private JButton createGroupButton;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;

    public AddGroupUI() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Group");

        JLabel groupNameLabel = new JLabel("Group Name:");
        groupNameField = new JTextField(20);

        JLabel userNameLabel = new JLabel("User Name:");
        userNameField = new JTextField(20);

        addUserButton = new JButton("Add User");
        createGroupButton = new JButton("Create Group");

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);

        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUserActionPerformed(e);
            }
        });

        createGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createGroupActionPerformed(e);
            }
        });

        JPanel panel = new JPanel();
        panel.add(groupNameLabel);
        panel.add(groupNameField);
        panel.add(userNameLabel);
        panel.add(userNameField);
        panel.add(addUserButton);
        panel.add(new JScrollPane(userList));
        panel.add(createGroupButton);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    private void addUserActionPerformed(ActionEvent evt) {
        String userName = userNameField.getText().trim();
        if (!userName.isEmpty()) {
            userListModel.addElement(userName);
            userNameField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "User name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createGroupActionPerformed(ActionEvent evt) {
        String groupName = groupNameField.getText().trim();
        if (!groupName.isEmpty()) {
            Group group = new Group(groupName);
            for (int i = 0; i < userListModel.size(); i++) {
                group.addUser(userListModel.getElementAt(i));
            }
            JOptionPane.showMessageDialog(this, "Group '" + group.getName() + "' created with " + group.getUsers().size() + " users.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Group name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JButton getAddUserButton() {
        return addUserButton;
    }

    public JButton getCreateGroupButton() {
        return createGroupButton;
    }

    public String getUserNameInput() {
        return userNameField.getText().trim();
    }

    public void addUserToList(String user) {
        userListModel.addElement(user);
    }

    public String getGroupNameInput() {
        return groupNameField.getText().trim();
    }

    public java.util.List<String> getUserList() {
        return java.util.Collections.list(userListModel.elements());
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new AddGroupUI().setVisible(true));
    }
}