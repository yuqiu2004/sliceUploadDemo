package org.yuqiu.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.minio.CreateMultipartUploadResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListPartsResponse;
import io.minio.http.Method;
import io.minio.messages.Part;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.yuqiu.conf.MinioProperties;
import org.yuqiu.conf.SliceMinioClient;
import java.util.*;

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

    public HashMap<Integer, String> getUploadUrl(List<Integer> list, String objectName, String uploadId) {
        HashMap<Integer, String> map = new HashMap<>();
        HashMap<String, String> params = new HashMap<>();
        params.put("uploadId", uploadId);
        list.forEach( i -> {
            params.put("partNumber", i.toString());
            try {
                String presignedObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(minioProperties.getBucketName())
                        .object(objectName)
                        .extraQueryParams(params)
                        .expiry(60 * 60 * 24)
                        .build());
                map.put(i, presignedObjectUrl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return map;
    }

    public void completeMultipartUpload(String uploadId, String objectName) {
        try {
            ListPartsResponse parts = minioClient.listParts(
                        minioProperties.getBucketName(),
                        null,
                        objectName,
                        null,
                        null,
                        uploadId,
                        null,
                        null
                );
            minioClient.completeMultipartUpload(
                    minioProperties.getBucketName(),
                    "",
                    objectName,
                    uploadId,
                    parts.result().partList().toArray(Part[]::new),
                    null,
                    null
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void abortMultipartUpload(String uploadId, String objectName) {
        try {
            minioClient.abortMultipartUpload(minioProperties.getBucketName(),
                    null,
                    objectName,
                    uploadId,
                    null,
                    null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> getFileUploadUrl(String objectName, String contentType) {
        HashMap<String, String> map = new HashMap<>();
        Multimap<String, String> headers = ArrayListMultimap.create();
        headers.put("Content-Type", contentType);
        String presignedObjectUrl = null;
        int idx = objectName.lastIndexOf(".");
        objectName = objectName.substring(0, idx) + "-" + UUID.randomUUID().toString() + objectName.substring(idx);
        try {
            presignedObjectUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.PUT)
                    .bucket(minioProperties.getBucketName())
                    .object(objectName)
                    .expiry(60 * 60 * 24)
                    .extraQueryParams(headers)
                    .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        map.put("url", presignedObjectUrl);
        map.put("objectName", objectName);
        return map;
    }
}
