package com.leyou.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.controller.UploadController;
import com.leyou.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Srd
 * @date 2020/5/20  15:31
 */
@Service
public class UploadServiceImpl implements UploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    /**
     * 支持的文件类型
     */
    private static final List<String> SUFFIXES = Arrays.asList("image/png", "image/jpeg","image/gif");

    @Autowired
    FastFileStorageClient storageClient;

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @Override
    public String upload(MultipartFile file) {

        try {
//            校验--文件类型
            String type = file.getContentType();
            if (!SUFFIXES.contains(type)) {
                LOGGER.info("上传失败，文件类型不匹配：{}", type);
                return null;
            }
//            校验--图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null){
                LOGGER.info("上传失败，文件内容不符合要求");
                return null;
            }

//            上传图片到本地
//            String url = uploadToLocal(file);

//            将图片上传到FastDFS
            String url = uploadToFdfs(file);

            return url;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 将图片上传到FastDFS
     * @param file
     * @return
     * @throws IOException
     */
    private String uploadToFdfs(MultipartFile file) throws IOException {
//        1. 获取文件后缀名
        String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
//        2. 上传
        StorePath storePath = this.storageClient.uploadFile(
                file.getInputStream(), file.getSize(), extension, null);
//        3. 返回完整路径
        return "http://image.leyou.com/" + storePath.getFullPath();
    }

    /**
     * 上传图片到本地
     * @param file
     * @return
     * @throws IOException
     */
    private String uploadToLocal(MultipartFile file) throws IOException {
//        保存图片--创建保存目录
        File dir = new File("C:/test/upload");
        if (!dir.exists()){
            dir.mkdirs();
        }
//        保存图片--保存
        file.transferTo(new File(dir,file.getOriginalFilename()));
//        拼接图片地址
        return "http://image.leyou.com/" + file.getOriginalFilename();
    }
}
