package org.yuqiu.conf;

import io.minio.MinioClient;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfiguration {

    @Resource
    private MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient(){
        String endpoint = minioProperties.getEndpoint();
        String accessKey = minioProperties.getAccessKey();
        String secretKey = minioProperties.getSecretKey();
        MinioClient minioClient = MinioClient.builder().endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        return new SliceMinioClient(minioClient);
    }
}
