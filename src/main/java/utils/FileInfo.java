/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Khoa
 */
import java.io.Serializable;
 
public class FileInfo implements Serializable {
    private static final long serialVersionUID = 1L;
 
    private String destinationDirectory;
    private String sourceDirectory;
    private String filename;
    private long fileSize;
    private int piecesOfFile;
    private int lastByteLength;
    private byte[] dataBytes;
    private String status;
    // Constructor
    // getter & setter

    public void setFilename(String name) {
        filename = name;
    }

    public void setDataBytes(byte[] fileBytes) {
        dataBytes = fileBytes;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public byte[] getDataBytes() {
        return dataBytes;
    }
    

    public void setDestinationDirectory(String destinationDir) {
        destinationDirectory = destinationDir;
    }

    public String getDestinationDirectory() {
        return this.destinationDirectory;
    }

    public String getFilename() {
        return this.filename;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getPiecesOfFile() {
        return piecesOfFile;
    }

    public void setPiecesOfFile(int piecesOfFile) {
        this.piecesOfFile = piecesOfFile;
    }

    public int getLastByteLength() {
        return lastByteLength;
    }

    public void setLastByteLength(int lastByteLength) {
        this.lastByteLength = lastByteLength;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
