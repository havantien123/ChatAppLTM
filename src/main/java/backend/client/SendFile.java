/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.client;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Khoa
 */
public class SendFile extends Thread{
    private static int allowSending;  // 0: waiting, 1: accept, 0: reject
    private String filepath;
    private String filename;
    private MessageSender sender;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private int portReceiveFile = 56789;
    private String send_address;
    private int time_out;
    private InputStream in;
    private  PeerHandler peerHandler;
    public SendFile(MessageSender send_mess , String filename , String path, PeerHandler peerHandler){
        this.sender = send_mess;
        this.filename = filename;
        this.filepath = path;
        this.peerHandler = peerHandler;

        this.time_out = 10;
        allowSending = 0;
    }

    
    @Override
    public void run(){
        try {
            sending();
        } catch (IOException ex) {
            Logger.getLogger(SendFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sending() throws IOException{
        sender.sendMessage("SendFile");
        System.out.println("Send request send file");
        while (allowSending == 0 && time_out != 0){
            System.out.println("Waiting for response");
            try {
                TimeUnit.SECONDS.sleep(1);
                time_out --;
            } catch (InterruptedException ex) {
                Logger.getLogger(SendFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (allowSending == 2 || time_out == 0)
        {
            sender.sendMessage("TimeOutReceiveFile");
            JOptionPane.showMessageDialog(null, "Friend reject send file." , "Send file", JOptionPane.OK_OPTION);
        }
        else {
            System.out.println("Connect to client "+ send_address +" "+ portReceiveFile);
       // Kết nối đến máy chủ
            Socket server = new Socket();
            server.connect(new InetSocketAddress(send_address, portReceiveFile));
            System.out.println("Start send file to friend...");
            sendingFile(server);
            server.close();
            System.out.println("Finish sending file to friend...");
        }
    }
     
    
    public void allowSending(String add, int port){
        System.out.println("[Send file]: Accept sending");
        allowSending = 1;
        send_address = add;
        portReceiveFile = port;
    }
    
    public void rejectSending(){
        System.out.println("[Send file]: Reject sending");
        allowSending = 2;
    }
    
    private void sendingFile(Socket server) throws IOException {
        InetAddress myIP = InetAddress.getLocalHost();; // Lấy địa chỉ IP của máy
        String myLocalIP = myIP.getHostAddress();
        try {
            in = new BufferedInputStream(new FileInputStream(filepath));
            System.out.println("filepath"+filepath+"filename"+filename);
            int len;
            byte[] temp = new byte[12345];
            DataOutputStream os = new DataOutputStream(server.getOutputStream());
            while (((len = in.read(temp)) > 0)) {
                // Chuyển đổi dữ liệu byte sang chuỗi để hiển thị
                String dataRead = new String(temp, 0, len);
                System.out.println("Data read: " + dataRead);
                if (len < 12345) {
                    byte[] extra;
                    // Sao chép dữ liệu hợp lệ vào mảng byte mới với độ dài chính xác
                    extra = Arrays.copyOf(temp, len);
                    System.out.println("[CLIENT] Finish send file");
                    System.out.println("extra"+ Base64.getEncoder().encodeToString(extra));
                    // Gửi phần cuối của tệp với thông báo chỉ ra kết thúc tệp

                    os.writeUTF("endfile," +myLocalIP+", "+ this.filename + "," + Base64.getEncoder().encodeToString(extra));
                    try {

                        this.peerHandler.addfile("("+myLocalIP+" ) "+filepath);
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }                } else {
                    // Gửi một phần của tệp chỉ ra rằng có thể có thêm dữ liệu
                    os.writeUTF("file," + Base64.getEncoder().encodeToString(temp));
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            server.close();
            this.in.close();
        }
    }
}
