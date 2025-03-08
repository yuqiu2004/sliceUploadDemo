package org.yuqiu.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("minio")
@Component
public class MinioProperties {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    public String getEndpoint() {
        return endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
