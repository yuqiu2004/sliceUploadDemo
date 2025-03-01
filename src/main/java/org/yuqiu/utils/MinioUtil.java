package org.yuqiu.utils;

import com.google.common.collect.Multimap;
import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.errors.*;
import io.minio.messages.Part;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.yuqiu.conf.MinioProperties;
import org.yuqiu.conf.SliceMinioClient;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MinioUtil {

    @Resource
    private SliceMinioClient minioClient;

    @Resource
    private MinioProperties minioProperties;


    public Map<String, String> createMultipartUpload(String region, String fileName, Multimap<String, String> headers, Multimap<String, String> extraQueryParams) {
        String uploadId = null;
        String objectName = UUID.randomUUID().toString() + '-' + fileName;
        try {
            CreateMultipartUploadResponse response = minioClient.createMultipartUpload(minioProperties.getBucketName(),
                    region,
                    objectName,
                    headers,
                    extraQueryParams);
            uploadId = response.result().uploadId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("uploadId", uploadId);
        map.put("objectName", objectName);
        return map;
    }

    public List<Integer> getUploadParts(String uploadId, String objectName) {
        List<Integer> list = null;
        try {
            ListPartsResponse parts =
                    minioClient.listParts(minioProperties.getBucketName(), "", objectName, null, null, uploadId, null, null);
            list = parts.result().partList().stream().map(Part::partNumber).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
