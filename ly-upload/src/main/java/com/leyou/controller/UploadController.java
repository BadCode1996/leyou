package com.leyou.controller;

import com.leyou.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Srd
 * @date 2020/5/20  15:22
 */
@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     * 上传图片
     *  请求方法：post
     *  请求路径：/upload/image
     *  请求参数：文件，file，springMvc会封装为一个接口，MultipleFile
     *  返回结果：上传成功后的url路径，String类型
     */
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file){
        String url = uploadService.upload(file);
        if (StringUtils.isBlank(url)){
//            url为空，上传失败，返回400
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
//        返回200，并携带url路径
        return ResponseEntity.ok(url);
    }
}
