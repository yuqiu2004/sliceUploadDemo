package org.yuqiu.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.yuqiu.Service.UploadService;
import org.yuqiu.pojo.Result;
import org.yuqiu.pojo.UploadDTO;

@RestController
@RequestMapping("/upload/multipart")
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
        return Result.success(uploadService.complete(uploadId, objectName));
    }

    @GetMapping("/cancel")
    public Result cancel(@RequestParam String uploadId, @RequestParam String objectName) {
        return Result.success(uploadService.cancel(uploadId, objectName));
    }
}
