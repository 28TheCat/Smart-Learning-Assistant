package com.wyt.controller;

import com.wyt.Utils.AliyunOSSOperator;
import com.wyt.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {
    private final AliyunOSSOperator aliyunOSSOperator;

    public UploadController(AliyunOSSOperator aliyunOSSOperator) {
        this.aliyunOSSOperator = aliyunOSSOperator;
    }

    @PostMapping
    public Result upload(MultipartFile file) throws Exception {
        log.info("接受参数：{}",file);
        if (!file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString().replace("-", "") + extName;
            // 上传文件
            String url = aliyunOSSOperator.upload(file.getBytes(), uniqueFileName);

            return Result.success( url);
        }
        return Result.error("上传失败");
    }
}
