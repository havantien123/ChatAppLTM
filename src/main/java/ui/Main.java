package ui;

import com.application.chatboxp2p.staticdata.Friend;
import com.application.chatboxp2p.staticdata.ListFriends;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Khoa
 */
public class Main {
    private static String public_ip;
    private static int port = 9876;

    public static void main(String[] args) {
//        createFriend();
//        getMyIPAddress();
//        ui.LoginUI loginUI = new ui.LoginUI(public_ip , port);
//        loginUI.setVisible(true);
        MainUI mainUI = new MainUI();
        mainUI.setVisible(false);

    }

    static void getMyIPAddress() {
        try {
            URL url_name = new URL("http://bot.whatismyipaddress.com");
            BufferedReader sc =
                    new BufferedReader(new InputStreamReader(url_name.openStream()));
            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("System IP Address : " + (localhost.getHostAddress()).trim());
            // reads system IPAddress 
            public_ip = sc.readLine().trim();
        } catch (Exception e) {
            public_ip = "Cannot Execute Properly";
        }
        System.out.println("Public IP Address: " + public_ip + "\n");
    }

    static void createFriend() {
        ListFriends listFriends = new ListFriends();
        listFriends.clear();
        listFriends.addUser(new Friend("Khoa Truong 0", "truongkhoa1799_0", 1));
        listFriends.addUser(new Friend("Khoa Truong 1", "truongkhoa1799_1", 1));
        listFriends.addUser(new Friend("Khoa Truong 2", "truongkhoa1799_2", 1));
        listFriends.addUser(new Friend("Khoa Truong 3", "truongkhoa1799_3", 0));
        listFriends.addUser(new Friend("Khoa Truong 4", "truongkhoa1799_4", 0));
        listFriends.addUser(new Friend("Khoa Truong 5", "truongkhoa1799_5", 0));
        listFriends.addUser(new Friend("Khoa Truong 6", "truongkhoa1799_6", 0));
        listFriends.addUser(new Friend("Khoa Truong 7", "truongkhoa1799_7", 0));

    }
}

