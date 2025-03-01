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
     * @param uploadDTO
     * @return
     */
    @PostMapping("/prepare")
    public Result prepare(@RequestBody UploadDTO uploadDTO) {
        return Result.success(uploadService.prepare(uploadDTO));
    }

    @GetMapping("/complete")
    public Result complete(@RequestParam String fileHash) {
        return Result.success(uploadService.complete(fileHash));
    }
}
