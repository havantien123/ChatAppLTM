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
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatServer {
    private ArrayList<ClientHandler> clientHandlerList = new ArrayList<>();
    private HashMap<String, ClientInfoServer> clientList = new HashMap<>();
    private final Map<String, ChatGroup> chatGroups = new HashMap<>();
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
//Tạo tài khoản
    public void createAccount(String username, String password) {
        LinkedList<String> empty = new LinkedList<>();
        // Tạo tài khoản trên server
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
        // Lặp qua tất cả các key (tên người dùng) trong clientList
        for (String key : clientList.keySet()) {
            // Kiểm tra nếu key hiện tại bằng với friendName
            if (key.equals(friendName)) {
                // Lặp qua danh sách bạn bè của username
                for (String friend : clientList.get(username).getFriendList()) {
                    // Kiểm tra nếu friendName đã là bạn của username hoặc friendName là username
                    if (friend.equals(friendName) || friendName.equals(username)) return;
                }
                // Thêm friendName vào danh sách bạn bè của username
                clientList.get(username).getFriendList().add(friendName);
                // Thêm username vào danh sách bạn bè của friendName
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
// kiểm tra tên người dùng đã tồn tại chưa
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

    public boolean createGroup(String groupName) {
        if (chatGroups.containsKey(groupName)) {
            return false; // Group already exists
        }
        chatGroups.put(groupName, new ChatGroup(groupName));
        return true;
    }

    public ChatGroup getGroup(String groupName) {
        return chatGroups.get(groupName);
    }

    public boolean addUserToGroup(String groupName, String username) {
        ChatGroup group = chatGroups.get(groupName);
        if (group != null) {
            return group.addMember(username);
        }
        return false; // Group not found
    }

    public boolean removeUserFromGroup(String groupName, String username) {
        ChatGroup group = chatGroups.get(groupName);
        if (group != null) {
            return group.removeMember(username);
        }
        return false; // Group not found
    }


    public void start() throws IOException {
        System.out.println("[SERVER] Start server.");
        initServer();
        // Tạo một Executor với một Fixed Thread Pool có số lượng thread cố định (numThreads)
        Executor executor = Executors.newFixedThreadPool(this.numThreads);
        try {
            // Tạo một ServerSocket lắng nghe trên cổng serverPort
            serverSocket = new ServerSocket(this.serverPort);
            while (true) {
                Socket client = serverSocket.accept();
                // Tạo một ClientHandler mới để xử lý kết nối của client
                ClientHandler c = new ClientHandler(this, client);
                // Thêm ClientHandler vào danh sách quản lý các client
                clientHandlerList.add(c);
                // Sử dụng executor để chạy ClientHandler trong một thread riêng
                executor.execute(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}