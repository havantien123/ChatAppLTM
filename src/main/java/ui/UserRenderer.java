package ui;

import com.application.chatboxp2p.staticdata.Friend;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Khoa
 */
public class UserRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object> {
    final String another_path = "/src/main/java/images/";
    final String path_win = "\\src\\main\\java\\images\\";
    String os = System.getProperty("os.name").toLowerCase();
    final private String dir;

    public UserRenderer() {
        if (os.equals("windows 10")){
            dir = System.getProperty("user.dir") + path_win;
        }
        else{
            dir = System.getProperty("user.dir") + another_path;
        }
    }

    public Component getListCellRendererComponent(JList<?> list, Object friend, int index, boolean isSelected, boolean hasFocus) {
        Friend f = (Friend) friend;
        setText(f.getName());
        if (f.getStatus() == 1) {
            setIcon(new javax.swing.ImageIcon(dir + "online_icon.png"));
        }else if (f.getStatus() == 0) {
            setIcon(new javax.swing.ImageIcon(dir + "offline_icon.png"));
        }else if (f.getStatus() == 2) {
            setIcon(new javax.swing.ImageIcon(dir + "notification_icon.png"));
        }
        setIconTextGap(10);
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(true);
        setFont(list.getFont());
        return this;
    }

}
