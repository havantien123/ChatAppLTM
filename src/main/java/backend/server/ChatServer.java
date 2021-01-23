package backend.server;

import utils.ClientInfo;
import utils.ClientInfoServer;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatServer {
    private ArrayList<ClientHandler> clientHandlerList = new ArrayList<>();
    private HashMap<String, ClientInfoServer> clientList = new HashMap<>();
    private int serverPort;
    private ServerSocket serverSocket;
    private int numThreads = 10;

    public ChatServer(int port) {
        this.serverPort     = port;
        this.serverSocket   = null;
    }

    public ChatServer(int port, int num) {
        this.serverPort     = port;
        this.serverSocket   = null;
        this.numThreads     = num;
    }

    public void initServer() throws IOException {

        File folder = new File("dtb");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            FileInputStream fis = new FileInputStream(file);
            XMLDecoder decoder = new XMLDecoder(fis);
            ClientInfoServer tempClientInfo = (ClientInfoServer) decoder.readObject();
            ClientInfoServer temp = new ClientInfoServer(tempClientInfo.getClientName(),
                    tempClientInfo.getClientPassword(), tempClientInfo.getClientStatus(), tempClientInfo.getFriendList());
            clientList.put(tempClientInfo.getClientName(), temp);
            decoder.close();
            fis.close();
        }
        //----------------------------TEST ZONE------------------------//
//        String user = "khuong";
//        System.out.println(getFriendList(user));
//        String temp = System.getProperty("os.name");
//        System.out.println(temp);
//        String password = "789";
//        String name = "tom";
//        createAccount(user, password);
//        addFriend(name  , "tom");
//        addFriend(name, "khuong");
//        addFriend(name, "khoa");
//        addFriend(name, "tuan");
//        removeFriend(name, "thien");
//        addFriend(name, "tuan");
//        for (String key : clientList.keySet()) {
//            ClientInfoServer temp = clientList.get(key);
//            System.out.println(temp.getClientName());
//            System.out.println(temp.getFriendList());
//        }
//        ClientInfoServer anotherTemp = clientList.get(name);
//        System.out.println(anotherTemp.getClientName());
//        System.out.println(anotherTemp.getFriendList());
//        logEverything();
        //-------------------------------------------------------------//
    }

    public ClientHandler getClientHandler (String name) {
        synchronized (this) {
            for(ClientHandler c: clientHandlerList) {
                if (c.getClientInfo() != null) {
                    if(c.getClientInfo().getClientName().equals(name)) {
                        if (this.clientList.get(name).getClientStatus().equals("on")) {
                            return c;
                        }
                    }
                }
            }
        }
        return null;
    }

//    public int getNumFriend(String username) {
//        return clientList.get(username).getFriendList().size();
//    }

    public void createAccount(String username, String password) {
        LinkedList<String> empty = new LinkedList<>();
        ClientInfoServer newClient = new ClientInfoServer(username, password, "on", empty);
        clientList.put(username, newClient);
    }

    public void markOnline(String username) {
        String status = "on";
        clientList.get(username).setClientStatus(status);
    }

    public void markOffline(String username) {
        String status = "off";
        clientList.get(username).setClientStatus(status);
    }

//    public LinkedList<String> findOnlineFriend(String username) {
//        ClientInfoServer temp = clientList.get(username);
//        LinkedList<String> tempList = new LinkedList<>();
//        for (String friend : temp.getFriendList()) {
//            if (clientList.get(friend).getClientStatus().equals("on")) {
//                tempList.add(clientList.get(friend).getClientName());
//            }
//        }
//        return tempList;
//    }

    public void addFriend(String username, String friendName) {
        for (String key : clientList.keySet()) {
            if (key.equals(friendName)) {
                for (String friend : clientList.get(username).getFriendList()) {
                    if (friend.equals(friendName) || friendName.equals(username)) return;
                }
                clientList.get(username).getFriendList().add(friendName);
                clientList.get(friendName).getFriendList().add(username);
            }
        }
    }

    public void removeFriend(String username, String friendName) {
       for (int i = 0; i < clientList.get(username).getFriendList().size(); i++) {
           if (clientList.get(username).getFriendList().get(i).equals(friendName)) {
               clientList.get(username).getFriendList().remove(i);
           }
        }
        for (int i = 0; i < clientList.get(friendName).getFriendList().size(); i++) {
            if (clientList.get(friendName).getFriendList().get(i).equals(username)) {
                clientList.get(friendName).getFriendList().remove(i);
            }
        }
    }

    public int checkPassword(String username, String password) {
        ClientInfoServer value = clientList.get(username);
        if (value == null) {
            return 1;
        }
        if (!password.equals(value.getClientPassword())) {
            return 2;
        }
        if (value.getClientStatus().equals("on")) {
            return 3;
        }
        return 0;
    }

    public boolean findUsername(String username) {
        ClientInfoServer value = clientList.get(username);
        return value != null;
    }

    public void logEverything() throws IOException {
        for (String key : clientList.keySet()) {
            ClientInfoServer temp = clientList.get(key);
            markOffline(key);
            FileOutputStream fos = new FileOutputStream(new File("dtb/" + key + ".xml"));
            XMLEncoder encoder = new XMLEncoder(fos);
            encoder.writeObject(temp);
            encoder.close();
            fos.close();
        }
    }

    public String getClientStatus (String username) {
        return clientList.get(username).getClientStatus();

    }

    public LinkedList<String> getFriendList (String username) {
        return clientList.get(username).getFriendList();
    }

    public void removeClientHandler(ClientHandler clientHandler) {
        synchronized (this) {
            this.clientHandlerList.remove(clientHandler);

        }
    }


    public void start() throws IOException {
        System.out.println("[SERVER] Start server.");
        initServer();
        Executor executor = Executors.newFixedThreadPool(this.numThreads);
        try {
            serverSocket = new ServerSocket(this.serverPort);
            while (true) {
                Socket client = serverSocket.accept();
                ClientHandler c = new ClientHandler(this, client);
                clientHandlerList.add(c);
                executor.execute(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}