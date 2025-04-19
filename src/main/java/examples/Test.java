package examples;

import backend.client.ChatClient;
import backend.server.ChatServer;
import controller.LoginController;
import ui.LoginUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static examples.RealHostIP.getRealHostIP;

public class Test {
    public static final String serverIPAddress =null;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Button Column Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400); // Tăng kích thước frame

        // Tạo panel với layout là BoxLayout theo chiều dọc
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Thêm khoảng trống xung quanh panel
        panel.setBackground(new Color(245, 245, 245)); // Màu nền panel

        // Tạo các nút button
        JButton button1 = new JButton("ReciveRequest");
        JButton button2 = new JButton("Send Request");
        JButton button3 = new JButton("About ");
        JButton button4 = new JButton("Exit");
button2.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String serverIPAddress = JOptionPane.showInputDialog(null, "Enter server IP Address:");

        LoginUI loginUI = new LoginUI();
        ChatClient chatClient = new ChatClient(serverIPAddress, 11111);
        LoginController loginController = new LoginController(loginUI, chatClient);
        loginController.initController();
        frame.dispose();

    }
});
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String myLocalIP = getRealHostIP(); // Sử dụng hàm getRealHostIP() ở trên
                    int port = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter Port Value:"));

                    JOptionPane.showMessageDialog(
                            frame,
                            "Server IP (Real Host): " + myLocalIP + "\n" +
                                    "Port: " + port
                    );

                    new ServerWorker(port).execute(); // Khởi động server
                } catch (SocketException ex) {
                    JOptionPane.showMessageDialog(frame, "Cannot detect real host IP: " + ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid port number!");
                }
            }
        });

        // Tùy chỉnh các nút button
        customizeButton(button1);
        customizeButton(button2);
        customizeButton(button3);
        customizeButton(button4);

        // Thêm các nút button vào panel với khoảng cách giữa chúng
        panel.add(button1);
        panel.add(Box.createRigidArea(new Dimension(0, 15))); // Thêm khoảng cách dọc
        panel.add(button2);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(button3);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(button4);

        // Căn giữa panel trong frame
        frame.setLayout(new GridBagLayout());
        frame.add(panel, new GridBagConstraints());

        // Hiển thị frame
        frame.setVisible(true);
    }

    // Phương thức tùy chỉnh button
    private static void customizeButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa button
        button.setBackground(new Color(70, 130, 180)); // Màu nền
        button.setForeground(Color.WHITE); // Màu chữ
        button.setFocusPainted(false); // Bỏ viền khi focus
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Font chữ
        button.setPreferredSize(new Dimension(250, 50)); // Kích thước button lớn hơn
    }

    // Lớp SwingWorker để chạy ChatServer trên một luồng riêng biệt
    private static class ServerWorker extends SwingWorker<Void, Void> {
        private int port;

        public ServerWorker(int port) {
            this.port = port;
        }

        @Override
        protected Void doInBackground() throws Exception {
            ChatServer chatServer = new ChatServer(port, 30);
            try {
                chatServer.start();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }
    }
}
