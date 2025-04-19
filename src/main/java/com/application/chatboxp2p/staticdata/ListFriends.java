package com.application.chatboxp2p.staticdata;

import javax.swing.DefaultListModel;
import com.application.chatboxp2p.staticdata.Friend;

/**
 * Manages the list of friends and groups.
 */
public class ListFriends {
    private DefaultListModel<Friend> list_friends = new DefaultListModel<>();


    public void addUser(Friend user) {
        list_friends.addElement(user);
    }

    public void addGroup(String groupName) {
        list_friends.addElement(new Friend(groupName, "group"));
    }

    public void removeUser(String name) {
        System.out.println(name);
        for (int i = 0; i < list_friends.getSize(); i++) {
            if (list_friends.elementAt(i).getUser_name().equals(name)) {
                list_friends.remove(i);
                break;
            }
        }
    }

    public DefaultListModel<Friend> getListModel() {
        return list_friends;
    }

    public void clear() {
        list_friends.clear();
    }

    public Friend getUserByIndex(int index) {
        return list_friends.get(index);
    }

    public void updateStatus(String user_name, int status) {
        for (int i = 0; i < list_friends.getSize(); i++) {
            if (list_friends.get(i).getUser_name().equals(user_name)) {
                list_friends.get(i).setStatus(status);
                break;
            }
        }
    }

    public void updateStatus(int index, int status) {
        list_friends.get(index).setStatus(status);
    }

    public DefaultListModel<Friend> getList_friends() {
        return this.list_friends;
    }
}