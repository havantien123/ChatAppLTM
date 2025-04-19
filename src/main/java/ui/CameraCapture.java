package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;

public class CameraCapture extends JFrame {
    static {
        // Đảm bảo rằng thư viện OpenCV được tải
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    JLabel userViewArea;
    boolean VideoToggled;
    boolean AudioToggled;
    VideoCapture camera;
    Timer timer;

    CameraCapture() {
        // Khởi tạo trạng thái Video và Audio
        VideoToggled = true;
        AudioToggled = true;

        // Cấu hình JFrame
        setLayout(null);
        setSize(new Dimension(640, 640));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        // Giao diện điều khiển video và audio
        JLabel j1 = new JLabel("User Video");
        j1.setBounds(25, 500, 100, 50);
        add(j1);
        JLabel j2 = new JLabel("User Audio");
        j2.setBounds(220, 500, 100, 50);
        add(j2);

        JRadioButton userVideoOn = new JRadioButton("ON", true);
        JRadioButton userVideoOff = new JRadioButton("OFF", false);
        ButtonGroup userVideo = new ButtonGroup();
        userVideo.add(userVideoOn);
        userVideo.add(userVideoOff);
        userVideoOn.setBounds(100, 500, 100, 30);
        userVideoOff.setBounds(100, 530, 100, 30);
        add(userVideoOn);
        add(userVideoOff);

        ButtonGroup userAudio = new ButtonGroup();
        JRadioButton userAudioOn = new JRadioButton("ON", true);
        JRadioButton userAudioOff = new JRadioButton("OFF", false);
        userAudio.add(userAudioOn);
        userAudio.add(userAudioOff);
        userAudioOn.setBounds(300, 500, 100, 30);
        userAudioOff.setBounds(300, 530, 100, 30);
        add(userAudioOn);
        add(userAudioOff);

        // Khu vực hiển thị video
        userViewArea = new JLabel();
        userViewArea.setBackground(Color.BLACK);
        userViewArea.setBounds(0, 0, 640, 480);
        add(userViewArea);

        // Nút chụp ảnh
        JButton captureButton = new JButton("Capture Image");
        captureButton.setBounds(420, 500, 150, 50);
        captureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                captureImage();
            }
        });
        add(captureButton);

        // Khởi tạo camera
        camera = new VideoCapture(0);  // Mở camera mặc định (index 0)
        if (!camera.isOpened()) {
            System.out.println("Camera không thể mở.");
            System.exit(0);
        }

        // Khởi động timer để lấy frame video mỗi 30ms
        timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mat frame = new Mat();
                if (camera.read(frame)) {
                    // Chuyển đổi frame từ Mat thành BufferedImage và hiển thị lên JLabel
                    BufferedImage image = matToBufferedImage(frame);
                    userViewArea.setIcon(new ImageIcon(image));
                }
            }
        });
        timer.start();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                stopCapture();  // Giải phóng camera và timer
                setVisible(false);  // Ẩn cửa sổ CameraCapture thay vì đóng hoàn toàn
            }
        });
    }


    // Chuyển đổi từ Mat (OpenCV) sang BufferedImage (Java)
    private BufferedImage matToBufferedImage(Mat mat) {
        // Kiểm tra kiểu dữ liệu của ảnh
        int width = mat.width();
        int height = mat.height();

        // Tạo một BufferedImage với kiểu ảnh RGB
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Lấy dữ liệu từ Mat (OpenCV sử dụng BGR)
        byte[] data = new byte[width * height * (int)mat.elemSize()];
        mat.get(0, 0, data);

        // Chuyển đổi từ BGR sang RGB và cập nhật vào BufferedImage
        int[] pixels = new int[width * height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int index = (i * width + j) * 3;
                // BGR -> RGB chuyển đổi
                int blue = data[index] & 0xFF;
                int green = data[index + 1] & 0xFF;
                int red = data[index + 2] & 0xFF;

                // Thiết lập pixel với giá trị RGB
                pixels[i * width + j] = (red << 16) | (green << 8) | blue;
            }
        }

        // Cập nhật ảnh từ mảng pixels vào BufferedImage
        image.setRGB(0, 0, width, height, pixels, 0, width);

        return image;
    }

    // Lưu ảnh khi chụp
    public void captureImage() {
        // Lấy frame hiện tại từ camera
        Mat frame = new Mat();
        if (camera.read(frame)) {
            BufferedImage image = matToBufferedImage(frame);
            try {
                // Lưu ảnh vào file
                String filePath = "D://"+ UUID.randomUUID().toString() +".png";  // Đường dẫn tới ổ D

                 // Lưu ảnh vào file
                 ImageIO.write(image, "png", new File(filePath));

                 // Hiển thị thông báo đường dẫn đã lưu ảnh
                 JOptionPane.showMessageDialog(this, "Image captured successfully and saved to: " + new File(filePath).getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage());
            }
        }
    }
    public void stopCapture() {
        if (camera.isOpened()) {
            camera.release();
        }
        if (timer != null) {
            timer.stop();
        }
    }
 public static void main(String[] args) {
	new CameraCapture();
}
}
