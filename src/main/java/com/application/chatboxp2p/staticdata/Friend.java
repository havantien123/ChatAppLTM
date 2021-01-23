/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.application.chatboxp2p.staticdata;

/**
 *
 * @author Khoa
 */
public class Friend {
    static int no = 0;
    private int status;
    private String name;
    private String user_name;

    public Friend(String name ,String user_name , int status ){
        this.name = name;
        this.status = status;
        this.user_name = user_name;

    }

    public Friend(String username, String stt) {
        this.name = username;
        this.user_name = username;
        if (stt.equals("on")) {
            this.status = 1;
        } else if (stt.equals( "off" )){
            this.status = 0;
        } else if (stt.equals("notify")){
            this.status = 2;
        }
    }

    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    
    
    
}
