package backend.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class RequestSender implements Runnable{
    private ChatClient chatClient;
    private DataOutputStream sender;
    private Queue<String> requestQueue = new LinkedList<String>();

    public RequestSender(ChatClient chatClient, DataOutputStream sender) {
        this.chatClient = chatClient;
        this.sender = sender;
    }
    @Override
    public void run() {
        try{
            while (true) {
                synchronized (this) {
                    while(!this.requestQueue.isEmpty()) {
                        String request = requestQueue.remove();
                        System.out.println(String.format("[CLIENT] Send request: %s", request));
                        sender.writeUTF(request);
                        sender.flush();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendRequest(String request) {
        synchronized (this) {
            return this.requestQueue.add(request);
        }
    }

    public boolean signUp(String username, String password) {
        return this.sendRequest("signup-" + username + "-" + password);
    }

    public boolean logIn(String username, String password) {
        return this.sendRequest("signup-" + username + "-" + password);
    }

    public boolean addFriend(String friendName) {
        return this.sendRequest("addfriend-" + friendName);
    }

    public boolean connectFriend(String friendName) {
        return this.sendRequest("connectfriend-" + friendName);
    }
}
