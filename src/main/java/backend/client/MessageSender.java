package backend.client;

import utils.ClientInfo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Queue;

public class MessageSender implements Runnable{
    private ClientInfo hostClient;
    private DataOutputStream sender;
    private Queue<String> messageQueue = new LinkedList<String>();
    private volatile boolean isChatting = true;
    public MessageSender(DataOutputStream sender, ClientInfo hostClient) {
        this.sender = sender;
        this.hostClient = hostClient;
    }


    @Override
    public void run() {
        while (isChatting) {
            synchronized (this) {
                while(!this.messageQueue.isEmpty()) {
                    String request = messageQueue.remove();
                    System.out.print

                            (String.format("[CLIENT] Send message: %s", request));
                    try {
                        sender.writeUTF(request);
                        sender.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Client is disconnected, cannot send mess");
                    }
                }
            }
        }
//        try{
//            while (isChatting) {
//                synchronized (this) {
//                    while(!this.messageQueue.isEmpty()) {
//                        String request = messageQueue.remove();
//                        System.out.print(String.format("[CLIENT] Send message: %s", request));
//                        sender.writeUTF(request);
//                        sender.flush();
//                    }
//                }
//            }
//            System.out.print("[CLIENT] Disconnect to peer\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void sendMessage(String cnt) {
        String mess = cnt + "-" + this.hostClient.getClientName() + "-" + new Timestamp(System.currentTimeMillis()).toString();
        System.out.println("this.hostClient.getClientName()"+this.hostClient.getClientName());
        synchronized (this) {
            this.messageQueue.add(mess);
        }
    }

    public void stop() {
        this.isChatting = false;
    }
}
