package org.yuqiu.pojo;

import java.util.List;

public class UploadStatus {
    private int status;
    private List<Integer> trunks;

    public UploadStatus(int status, List<Integer> trunks) {
        this.status = status;
        this.trunks = trunks;
    }
    public UploadStatus() {}

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Integer> getTrunks() {
        return trunks;
    }

    public void setTrunks(List<Integer> trunks) {
        this.trunks = trunks;
    }
}
