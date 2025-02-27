package org.yuqiu.Service;

import io.minio.MinioClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.yuqiu.pojo.UploadStatus;

@Service
public class UploadService {

    @Resource
    private MinioClient minioClient;

    /**
     * 模拟的当前用户id
     */
    private static final int userId = 123;

    public UploadStatus prepare(String fileHash) {
        // 此处从缓存中尝试获取上传的进度
        String uploadId = "UPLOAD_" + userId + "_" + fileHash;
        return new UploadStatus(0, null); // 返回空表示没有上传进度 需要从头开始上传
    }



}
