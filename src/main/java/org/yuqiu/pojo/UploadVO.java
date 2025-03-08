package org.yuqiu.pojo;

import java.util.Map;

public class UploadVO {
    private String uploadId;
    private String objectName;
    private Map<Integer, String> trunkMap;

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Map<Integer, String> getTrunkMap() {
        return trunkMap;
    }

    public void setTrunkMap(Map<Integer, String> trunkMap) {
        this.trunkMap = trunkMap;
    }

    public UploadVO(String uploadId, String objectName, Map<Integer, String> trunkMap) {
        this.uploadId = uploadId;
        this.objectName = objectName;
        this.trunkMap = trunkMap;
    }
}
