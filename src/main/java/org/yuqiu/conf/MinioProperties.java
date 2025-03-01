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
}
