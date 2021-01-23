/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.client;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import utils.FileInfo;

/**
 *
 * @author Khoa
 */
public class ReceiveFile extends Thread{
    private MessageSender sender;
    private int portReceiveFile = 56789;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private static boolean time_out;
    private StringBuilder fileStringBuilder = new StringBuilder();
    private DataInputStream is;
    private boolean isSending = false;
    public ReceiveFile(MessageSender send_mess){
        this.sender = send_mess;
        time_out = true;
    }
    
    @Override
    public void run(){
        try {
            receiving();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void receiving() throws IOException{
        int accept = JOptionPane.showConfirmDialog(null, "Accept file?", "Send file", JOptionPane.YES_NO_OPTION);
        if (!time_out)
        {
            time_out = true;
            JOptionPane.showMessageDialog(null, "Time out receive file." , "Receive file", JOptionPane.OK_OPTION);
        }
        else if (accept == 1)
            this.sender.sendMessage("RejectSendFile");
        else if (accept == 0 && time_out){
            ServerSocket serverSocket = new ServerSocket(portReceiveFile);

            this.sender.sendMessage("AcceptSendFile-" + String.valueOf(portReceiveFile));
            portReceiveFile++;

            System.out.println("Waiting for connect");
            Socket client = serverSocket.accept();
            this.is = new DataInputStream(client.getInputStream());
            System.out.println("Accepted connection");
            this.isSending = true;
            receivingFile(client);

            client.close();
            serverSocket.close();
            System.out.println("Done receiving file from friend");
        }
        
    }
    
    private void receivingFile(Socket client) throws IOException{
        try {
                // receive file info
//                ois = new ObjectInputStream(client.getInputStream());
//                FileInfo fileInfo = (FileInfo) ois.readObject();
            String home = System.getProperty("user.home");
            String name_os = System.getProperty("os.name").toLowerCase();
            String dir = home;
            if (name_os.equals("windows 10"))
            {
                dir += "\\Downloads\\";
            }
            else{
                dir = dir + "/Downloads/";
            }
            String message      = null;
            String[] segments   = null;
            String fileName     = null;
            OutputStream out = new BufferedOutputStream(new FileOutputStream(dir + "Unconfirmed"));
            while(this.isSending) {
                message = is.readUTF();
                segments = StringUtils.split(message, ',');
                if (segments != null && segments.length > 0) {
                    String type = segments[0];
                    switch (type) {
                        case "file": {
//                            System.out.println("RECEIVING FILE");
                            String content = message.substring(message.indexOf(",") + 1);
                            out.write(Base64.getDecoder().decode(content));
//                            this.fileStringBuilder = new StringBuilder();
//                            this.fileStringBuilder.append(content);
                            break;
                        }
                        case "endfile": {
                            String content = message.substring(message.indexOf(",") + 1);
                            String finalcontent = content.substring(content.indexOf(",") + 1);
                            System.out.println(finalcontent);
//                            fileStringBuilder.append(finalcontent);
                            out.write(Base64.getDecoder().decode(finalcontent));
                            fileName = segments[1];
                            System.out.println(fileName);
                            File file = new File(dir+ "Unconfirmed");
                            File newFile = new File(dir + fileName);
                            if(file.renameTo(newFile)){
                                System.out.println("File received success");;
                            }else{
                                System.out.println("File received failed");
                            }
                            out.close();
                            this.isSending = false;
                            break;
                        }
                    }
                }
            }
                  
        } catch (IOException e) {
            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
        } finally {
            client.close();
            closeStream(ois);
            closeStream(oos);
        }
    }
    
    private boolean createFile(FileInfo fileInfo) throws IOException {
        BufferedOutputStream bos = null;
         
        try {
            if (fileInfo != null) {
                String home = System.getProperty("user.home");
                String name_os = System.getProperty("os.name").toLowerCase();
                String dir = home;
                if (name_os.equals("windows 10"))
                {
                    dir += "\\Downloads\\";
                }
                else{
                    dir = dir + "/Downloads/";
                }
                File fileReceive = new File(dir + fileInfo.getFilename());
                bos = new BufferedOutputStream(new FileOutputStream(fileReceive));
                // write file content
                bos.write(fileInfo.getDataBytes());
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeStream(bos);
        }
        return true;
    }
 
    public void closeStream(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }
 
    public void closeStream(OutputStream outputStream) throws IOException {
        if (outputStream != null) {
                outputStream.close();
        }
    }
    
    public void timeOut(){
        System.out.println("[Send File]: Time out receive File");
        this.time_out = false;
    }
    
}
