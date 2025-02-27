package org.yuqiu.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yuqiu.Service.UploadService;
import org.yuqiu.pojo.Result;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Resource
    private UploadService uploadService;


    /*
      处理思路

        1. 前端计算文件的哈希值（md5），将文件分片，然后将fileHash传给后端查询上传进度
        2. 后端通过fileHash和userId拼接加密为uploadId
        3. 然后通过这个键值查询redis缓存中，是否存在对应的对象
          a. 如果存在 说明已经上传了部分 返回已经上传的进度
          b. 如果不存在 则返回0
        4. 通过上传接口上传分片，然后转发存储分片
        5. 上传完调用接口合并分片并清理

     */

    @GetMapping("/prepare")
    public Result prepare(@RequestParam String fileHash) {
        return Result.success(uploadService.prepare(fileHash));
    }
}
