package utils;

import java.util.LinkedList;

public class ClientInfoServer {
    private String clientName;
    private String clientPassword;
    private String clientStatus;
    private LinkedList<String> friendList;

    public ClientInfoServer() {

    }

    public ClientInfoServer(String clientName, String clientPassword, String clientStatus, LinkedList<String> friendList) {
        this.clientName = clientName;
        this.clientPassword = clientPassword;
        this.clientStatus = clientStatus;
        this.friendList = friendList;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientPassword() {
        return clientPassword;
    }

    public String getClientStatus() {
        return clientStatus;
    }

    public LinkedList<String> getFriendList() {
        return friendList;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }

    public void setClientStatus(String clientStatus) {
        this.clientStatus = clientStatus;
    }

    public void setFriendList(LinkedList<String> friendList) {
        this.friendList = friendList;
    }
}