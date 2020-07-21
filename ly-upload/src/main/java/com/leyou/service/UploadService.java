package com.leyou.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Srd
 * @date 2020/5/20  15:28
 */
public interface UploadService {

    /**
     * 上传图片
     * @param file
     * @return
     */
    String upload(MultipartFile file);
}
