package org.yuqiu.pojo;

public class UploadDTO {

    private String fileName;
    private String fileHash;
    private int trunk;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public int getTrunk() {
        return trunk;
    }

    public void setTrunk(int trunk) {
        this.trunk = trunk;
    }
}
