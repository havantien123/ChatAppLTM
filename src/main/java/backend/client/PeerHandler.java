package backend.client;

import backend.server.ChatServer;
import utils.ClientInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PeerHandler implements Runnable{
//    ClientInfo hostClient;
//    ClientInfo targetClient;
    private String targetClientName;
    ChatClient client;

    private Socket socket;

    private InputStream is;
    private OutputStream os;

    private MessageSender messageSender;
    private MessageReceiver messageReceiver;
    private Thread messageSenderThread;
    private Thread messageReceiverThread;
    private SendFile sendFileThread;
    private ReceiveFile receiveFile;

    private boolean isOpening = false;

    private JTextPane textPane;

    public PeerHandler(Socket socket, String targetClientName, ChatClient client , boolean stt) {
        this.socket = socket;
        this.isOpening = stt;
        this.targetClientName = targetClientName;
        this.client = client;
        this.textPane = new JTextPane();
        textPane.setSize(610, 370);
    }

    @Override
    public String toString() {
        return "PeerHandler{" +
                "targetClientName='" + targetClientName + '\'' +
                ", client=" + client +
                ", socket=" + socket +
                ", is=" + is +
                ", os=" + os +
                ", messageSender=" + messageSender +
                ", messageReceiver=" + messageReceiver +
                ", messageSenderThread=" + messageSenderThread +
                ", messageReceiverThread=" + messageReceiverThread +
                ", sendFileThread=" + sendFileThread +
                ", receiveFile=" + receiveFile +
                ", isOpening=" + isOpening +
                ", textPane=" + textPane +
                '}';
    }

    public void setStatusWindow(boolean stt){
        this.isOpening = stt;
    }

    public boolean getStatusWindow(){
        return this.isOpening;}

    public JTextPane getTextPane() {
        return this.textPane;}

    public ChatClient getClient() {
        return this.client;
    }

    public String getTargetClientName() {
        return this.targetClientName;
    }

    @Override
    public void run() {
        try {
            this.is = this.socket.getInputStream();

            this.os = this.socket.getOutputStream();
            this.messageSender = new MessageSender(new DataOutputStream(this.os), this.getClient().getClientInfo());
            this.messageSenderThread = new Thread(this.messageSender);
            this.messageReceiver = new MessageReceiver(new DataInputStream(this.is), this.getClient().getClientInfo(), this);
            this.messageReceiverThread = new Thread(this.messageReceiver);
            System.out.println("Nhan"+ this.getClient().getClientInfo());
            System.out.println("Gui"+ this.getClient().getClientInfo());

            this.messageSenderThread.start();
            this.messageReceiverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String mess) {
        String formattedMess = "message-" + mess;
        this.messageSender.sendMessage(formattedMess);
    }
public void addfile(String mess) throws BadLocationException {
    StyledDocument doc = this.textPane.getStyledDocument();
    Style style1 = this.textPane.addStyle("myStyle", null);
    Style style2 = this.textPane.addStyle("myStyle", null);

    // Tạo nhãn cho tên người gửi
    JLabel label = new JLabel(  "Me:");
    label.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 16));
    label.setForeground(new java.awt.Color(160, 28, 28)); // Màu cho tên người gửi (Me)
    StyleConstants.setComponent(style1, label);

    boolean isImage = false;
    ImageIcon icon = null;
    String filePath = null; // ✅ Di chuyển ra ngoài

    // Lấy chuỗi phần sau dấu ")", tách chuỗi để lấy đường dẫn tệp hình ảnh
    String[] parts = mess.split(" ", 2);  // Tách chuỗi thành 2 phần, phần 1 là địa chỉ IP, phần 2 là đường dẫn tệp
    if (parts.length > 1) {
        filePath = parts[1].trim();  // Lấy phần sau dấu cách đầu tiên và loại bỏ khoảng trắng thừa
        System.out.println(filePath);
        if (filePath.startsWith(")")) {
            filePath = filePath.substring(2).trim();  // Cắt dấu ") " ở đầu chuỗi
        }

        // Kiểm tra xem tệp có phải là hình ảnh hợp lệ không
        if (isValidImageFile(filePath)) {
            try {
                // Đọc và hiển thị hình ảnh
                File file = new File(filePath);
                BufferedImage img = ImageIO.read(file);
                if (img != null) {
                    // Thay đổi kích thước hình ảnh nếu nó quá to
                    int maxWidth = 100;  // Chiều rộng tối đa
                    int maxHeight = 100; // Chiều cao tối đa
                    int width = img.getWidth();
                    int height = img.getHeight();

                    if (width > maxWidth) {
                        height = (int) ((double) height / width * maxWidth);
                        width = maxWidth;
                    }
                    if (height > maxHeight) {
                        width = (int) ((double) width / height * maxHeight);
                        height = maxHeight;
                    }

                    // Tạo ImageIcon từ hình ảnh đã thay đổi kích thước
                    Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaledImg);
                    isImage = true;
                }
            } catch (IOException e) {
                // Không làm gì cả vì isImage vẫn là false
                e.printStackTrace();
            }
        }
    }

    if (isImage) {
        // Nếu là hình ảnh, hiển thị hình ảnh
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // con trỏ tay

        final String finalPath = filePath; // ✅ biến final để dùng trong lambda/inner class
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame previewFrame = new JFrame("Xem ảnh lớn");
                previewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                ImageIcon fullIcon = new ImageIcon(finalPath);
                JLabel fullImageLabel = new JLabel(fullIcon);
                JScrollPane scrollPane = new JScrollPane(fullImageLabel);

                previewFrame.add(scrollPane);
                previewFrame.setSize(600, 600); // hoặc theo kích thước ảnh
                previewFrame.setLocationRelativeTo(null);
                previewFrame.setVisible(true);
            }
        });

        StyleConstants.setComponent(style2, imageLabel);

    } else if (filePath != null) {
        final String finalFilePath = filePath;
        JLabel fileLabel = new JLabel(new File(finalFilePath).getName());
        fileLabel.setForeground(Color.BLUE.darker());
        fileLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fileLabel.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 14));
        fileLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().open(new File(finalFilePath));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Không thể mở file: " + ex.getMessage());
                }
            }
        });
        StyleConstants.setComponent(style2, fileLabel);
    } else {
        JTextArea textArea = new JTextArea(mess);
        textArea.setLineWrap(true);
        textArea.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 14));
        textArea.setEditable(false);
        StyleConstants.setComponent(style2, textArea);
    }


    synchronized (this) {
        doc.insertString(doc.getLength(), " ", style1);
        doc.insertString(doc.getLength(), "\n", style2);
    }

    }
    public void sendDisconnect() {
        System.out.println("Send disconnect to friend");
        this.messageSender.sendMessage("disconnect");
    }

    public void disconnect() {
        this.messageSender.stop();
        this.messageReceiver.stop();
        this.client.removePeerHandle(this);
    }
    public boolean isValidImageFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            try {
                // Thử đọc hình ảnh từ tệp
                BufferedImage img = ImageIO.read(file);
                return img != null;  // Nếu hình ảnh hợp lệ, trả về true
            } catch (IOException e) {
                return false;  // Nếu không thể đọc hình ảnh, trả về false
            }
        }
        return false;  // Nếu tệp không tồn tại hoặc không phải là tệp hợp lệ
    }

    // Phương thức thêm văn bản hoặc hình ảnh vào JTextPane
    public void addText(String mess) throws BadLocationException {
        StyledDocument doc = this.textPane.getStyledDocument();
        Style style1 = this.textPane.addStyle("myStyle", null);
        Style style2 = this.textPane.addStyle("myStyle", null);

        // Tạo nhãn cho tên người gửi
        JLabel label = new JLabel(this.targetClientName + ":");
        label.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 16));
        label.setForeground(new java.awt.Color(28, 160, 28));
        StyleConstants.setComponent(style1, label);

        boolean isImage = false;
        ImageIcon icon = null;
        String filePath = null;

        // Lấy chuỗi phần sau dấu ")", tách chuỗi để lấy đường dẫn tệp hình ảnh
        String[] parts = mess.split(" ", 2);  // Tách chuỗi thành 2 phần, phần 1 là địa chỉ IP, phần 2 là đường dẫn tệp
        if (parts.length > 1) {
            filePath = parts[1].trim();  // Lấy phần sau dấu cách đầu tiên và loại bỏ khoảng trắng thừa
            System.out.println(filePath);
            if (filePath.startsWith(")")) {
                filePath = filePath.substring(2).trim();  // Cắt dấu ") " ở đầu chuỗi
            }

            // Kiểm tra xem tệp có phải là hình ảnh hợp lệ không
            if (isValidImageFile(filePath)) {
                try {
                    // Đọc và hiển thị hình ảnh
                    File file = new File(filePath);
                    BufferedImage img = ImageIO.read(file);
                    if (img != null) {
                        // Thay đổi kích thước hình ảnh nếu nó quá to
                        int maxWidth = 100;  // Chiều rộng tối đa
                        int maxHeight = 100; // Chiều cao tối đa
                        int width = img.getWidth();
                        int height = img.getHeight();

                        if (width > maxWidth) {
                            height = (int) ((double) height / width * maxWidth);
                            width = maxWidth;
                        }
                        if (height > maxHeight) {
                            width = (int) ((double) width / height * maxHeight);
                            height = maxHeight;
                        }

                        // Tạo ImageIcon từ hình ảnh đã thay đổi kích thước
                        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        icon = new ImageIcon(scaledImg);
                        isImage = true;
                    }
                } catch (IOException e) {
                    // Không làm gì cả vì isImage vẫn là false
                    e.printStackTrace();
                }
            }
        }

        if (isImage && filePath != null) {
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            final String finalPath = filePath;
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JFrame previewFrame = new JFrame("Xem ảnh lớn");
                    previewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    ImageIcon fullIcon = new ImageIcon(finalPath);
                    JLabel fullImageLabel = new JLabel(fullIcon);
                    JScrollPane scrollPane = new JScrollPane(fullImageLabel);

                    previewFrame.add(scrollPane);
                    previewFrame.setSize(600, 600);
                    previewFrame.setLocationRelativeTo(null);
                    previewFrame.setVisible(true);
                }
            });

            StyleConstants.setComponent(style2, imageLabel);
        }  else if (filePath != null) {
            final String finalFilePath = filePath;
            JLabel fileLabel = new JLabel(new File(finalFilePath).getName());
            fileLabel.setForeground(Color.BLUE.darker());
            fileLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            fileLabel.setFont(new java.awt.Font("Times New Roman", Font.PLAIN, 14));
            fileLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().open(new File(finalFilePath));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Không thể mở file: " + ex.getMessage());
                    }
                }
            });
            StyleConstants.setComponent(style2, fileLabel);
        } else {
            JTextArea textArea = new JTextArea(mess);
            textArea.setLineWrap(true);
            textArea.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 14));
            textArea.setEditable(false);
            StyleConstants.setComponent(style2, textArea);
        }

        synchronized (this) {
            doc.insertString(doc.getLength(), " ", style1);
            doc.insertString(doc.getLength(), "\n", style2);
        }
    }
    public void sendFile(String path, String filename) {
        this.sendFileThread = new SendFile(messageSender , filename , path,this);
        this.sendFileThread.setDaemon(true);
        this.sendFileThread.start();
    }

    public void receiveFile(){
        this.receiveFile = new ReceiveFile(messageSender,this);
        this.receiveFile.setDaemon(true);
        this.receiveFile.start();
    }

    public void allowSending(String port)
    {
        sendFileThread.allowSending(this.socket.getInetAddress().getHostAddress(), Integer.parseInt(port));
    }
    public void rejectSending()
    {
        if (!sendFileThread.equals(null))
            sendFileThread.rejectSending();
    }
    public void timeOutReceiveFile(){
        this.receiveFile.timeOut();
    }
}
