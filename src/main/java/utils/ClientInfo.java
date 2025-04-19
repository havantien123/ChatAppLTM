package utils;

// This class use for Server to maintain a list of clients connected

import java.util.LinkedList;

public class ClientInfo {
    private String name;
    public LinkedList<PeerInfo> friendList;
//    private String clientStatus;

    public  ClientInfo(String name) {
        this.name     = name;
        this.friendList = new LinkedList<>();
//        this.clientStatus   = clientStatus;
    }

    public void addFriend(String friendName, boolean isActive) {
        PeerInfo newFriend = new PeerInfo(friendName);
        if (isActive) {
            newFriend.setOnline();
        }
        this.friendList.add(newFriend);
    }



    public void setClientName(String name) {this.name = name;}
    public String getClientName() {return this.name;}

//    public void setClientStatus(String clientStatus) {this.clientStatus = clientStatus;}
//    public String getClientStatus() {return this.clientStatus;}

//    public void setClientID(String clientID) {this.clientID = clientID;}
//    public String getClientID() {return this.clientID;}
}
