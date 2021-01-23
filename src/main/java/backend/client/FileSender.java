package backend.client;

import javax.swing.*;
import java.io.*;
import java.util.Arrays;
import java.util.Base64;

public class FileSender extends SwingWorker {
    private String path;
    private String filename;
    private PeerHandler peerHandler;
    public FileSender(String path, String filename, PeerHandler peerHandler) {
        this.path = path;
        this.filename = filename;
        this.peerHandler = peerHandler;
    }

    @Override
    protected Object doInBackground() throws Exception {
        try (InputStream in = new BufferedInputStream(new FileInputStream(path))) {
            int len;
            byte[] temp = new byte[12345];
            while (((len = in.read(temp)) > 0)) {
                if (len < 12345) {
                    byte[] extra;
                    extra = Arrays.copyOf(temp, len);
                    this.peerHandler.sendMessage("Endfile," + this.filename + "," + Base64.getEncoder().encodeToString(extra));
                } else {
                    this.peerHandler.sendMessage("File," + Base64.getEncoder().encodeToString(temp));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void done()
    {
        try
        {
            this.peerHandler.addText("File Done");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
