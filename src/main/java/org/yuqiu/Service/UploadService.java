package org.yuqiu.Service;

import ch.qos.logback.core.util.StringUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.yuqiu.conf.SliceMinioClient;
import org.yuqiu.pojo.UploadDTO;
import org.yuqiu.pojo.UploadRedisDTO;
import org.yuqiu.pojo.UploadResult;
import org.yuqiu.pojo.UploadVO;
import org.yuqiu.utils.MinioUtil;

import java.util.*;

@Service
public class UploadService {

    @Resource
    private SliceMinioClient minioClient;

    @Resource
    private MinioUtil minioUtil;

    /**
     * 模拟的当前用户id
     */
    private static final int userId = 123;

    public UploadVO prepare(UploadDTO uploadDTO) {
        // 此处从缓存中尝试获取上传的进度
        String key = "UPLOAD_" + userId + "_" + uploadDTO.getFileHash();
        UploadRedisDTO redisDTO = getRedisUploadId(key);
        String uploadId = null, objectName = null;
        if (null != redisDTO) {
            uploadId = redisDTO.getUploadId();
            objectName = redisDTO.getObjectName();
        }
        if (StringUtil.isNullOrEmpty(uploadId)) {
            // 创建分片上传请求 返回上传的路径
            Multimap<String, String> headers = HashMultimap.create();
            headers.put("Content-Type", uploadDTO.getContentType());
            Map<String, String> map = minioUtil.createMultipartUpload("", uploadDTO.getFileName(), headers, null);
            uploadId = map.get("uploadId");
            objectName = map.get("objectName");
            // uploadId存入redis缓存 记录uploadId
        }
        // 通过uploadId获取上传的进度
        List<Integer> list = getUploadedList(uploadId, objectName);
        // 返回uploadId、上传进度
        List<Integer> toUpload = new ArrayList<>();
        if (!list.isEmpty()) list.sort(Comparator.naturalOrder());
        int it = 1;
        for (Integer i : list) {
            while(it < i) {
                toUpload.add(it);
                it++;
            }
            it+=2;
        }
        while(it <= uploadDTO.getTrunk()) toUpload.add(it++);
        // 只获取还未上传的分片以及对应的请求路径
        HashMap<Integer, String> map = getUploadUrl(toUpload, objectName, uploadId);
        return new UploadVO(uploadId, objectName, map);
    }

    private HashMap<Integer, String> getUploadUrl(List<Integer> list, String objectName, String uploadId) {
        return minioUtil.getUploadUrl(list, objectName, uploadId);
    }

    private List<Integer> getUploadedList(String uploadId, String objectName) {
        return minioUtil.getUploadParts(uploadId, objectName);
    }

    private UploadRedisDTO getRedisUploadId(String key) {
        return new UploadRedisDTO(); // 从缓存中获取
    }


    public UploadResult complete(String uploadId, String objectName) {
        minioUtil.completeMultipartUpload(uploadId, objectName);
        return new UploadResult();
    }

    public String cancel(String uploadId, String objectName) {
        minioUtil.abortMultipartUpload(uploadId, objectName);
        return "CANCEL SUCCESS";
    }
}
