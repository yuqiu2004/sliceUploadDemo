package org.yuqiu.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.yuqiu.Service.UploadService;
import org.yuqiu.pojo.Result;
import org.yuqiu.pojo.UploadDTO;

@RestController
@RequestMapping("/upload/multipart")
@CrossOrigin
public class UploadController {

    @Resource
    private UploadService uploadService;

    /**
     * 初始化上传并获取上传进度
     * @param uploadDTO 上传dto
     * @return 返回封装内容 预上传路径
     */
    @PostMapping("/prepare")
    public Result prepare(@RequestBody UploadDTO uploadDTO) {
        return Result.success(uploadService.prepare(uploadDTO));
    }

    /**
     * 上传分片合并
     * @param uploadId
     * @param objectName
     * @return
     */
    @GetMapping("/complete")
    public Result complete(@RequestParam String uploadId, @RequestParam String objectName) {
        try {
            uploadService.complete(uploadId, objectName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.success();
    }

    @GetMapping("/cancel")
    public Result cancel(@RequestParam String uploadId, @RequestParam String objectName) {
        return Result.success(uploadService.cancel(uploadId, objectName));
    }

    @GetMapping("/getUploadUrl")
    public Result getUploadUrl(@RequestParam String objectName, @RequestParam String contentType) {
        return Result.success(uploadService.getSingleUploadUrl(objectName, contentType));
    }

    @GetMapping("/test")
    public Result test(String str) {
        return Result.success("hello, " + str);
    }
}
