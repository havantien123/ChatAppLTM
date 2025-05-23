package controller;

import backend.client.ChatClient;
import ui.LoginUI;
import ui.MainUI;

import javax.swing.*;

public class LoginController {
    LoginUI loginUI;
    ChatClient chatClient;

    public LoginController(LoginUI loginUI, ChatClient chatClient) {
        this.loginUI = loginUI;
        this.chatClient = chatClient;
    }

    public void initController() {
        //trả về true nếu thành công
        boolean checked = this.chatClient.start();
        if (checked) {
            this.loginUI.setVisible(true);
            this.loginUI.getLogin_but().addActionListener(e->login());
            this.loginUI.getPasswordField().addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    pass_inputKeyPressed(evt);
                }
            });

            this.loginUI.getSignup_but().addActionListener(e->signup());

        }
        else {
            this.loginUI.dispose();
        }
    }
    
    private void pass_inputKeyPressed(java.awt.event.KeyEvent evt) {                                      
        if (evt.getKeyCode() == 10)
            login();
    }

    private void signup() {
        String username = this.loginUI.getUser_signup_input().getText();
        String password = new String(this.loginUI.getPass_signup_input().getPassword());
        if (username.isEmpty() || password.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Invalid input!");
        }
        else {
            this.chatClient.sendReq("signup-" + username + "-"+ password);
        }
        String resMess = null;
        do {
            synchronized (this) {
                resMess = this.chatClient.getResponseMessage();
            }
        } while (resMess == null);
        if (resMess.equals("success-signup")) {
            this.loginUI.setVisible(false);
            MainUI mainUI = new MainUI();
            mainUI.setUserTitle(username);
            MainController mainController = new MainController(mainUI, this.chatClient);
            this.chatClient.addObserver(mainController);
            mainController.initController();
        }
        else {
            JOptionPane.showMessageDialog(null, "Signup failed.", "Signup", JOptionPane.OK_OPTION);
            this.loginUI.getUser_signup_input().setText("");
            this.loginUI.getPass_signup_input().setText("");
        }
        this.chatClient.setResponseMessage(null);
    }

    private void login() {
        //Lấy tên người dùng từ trường dữ liệu
        String username = this.loginUI.getUser_input().getText();
        //Lấy mật khẩu
        String password = new String(this.loginUI.getPass_input().getPassword());

        if (username.isEmpty() || password.isEmpty())
        {

            JOptionPane.showMessageDialog(null, "Invalid input!");
        }
        else {
            this.chatClient.sendReq("login-" + username + "-"+ password);
        }
        String resMess = null;
        do {
            synchronized (this) {
                resMess = this.chatClient.getResponseMessage();
                //nhận dữ liệu từ chatclient nếu là đăng nhập thành công
            }
        } while (resMess == null);
        if (resMess.equals("success-login")) {
            this.loginUI.dispose();
            MainUI mainUI = new MainUI();
            mainUI.setUserTitle(username);
            MainController mainController = new MainController(mainUI, this.chatClient);
            this.chatClient.addObserver(mainController);
            mainController.initController();
        }
        else {
            JOptionPane.showMessageDialog(null, "Login failed.", "Login", JOptionPane.OK_OPTION);
            this.loginUI.getUser_input().setText("");
            this.loginUI.getPass_input().setText("");
        }
        this.chatClient.setResponseMessage(null);
    }
}
