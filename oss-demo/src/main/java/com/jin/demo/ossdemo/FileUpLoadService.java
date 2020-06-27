package com.jin.demo.ossdemo;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUpLoadService {
    @Autowired
    private AliyunConfig aliyunConfig;
    @Autowired
    private OSSClient ossClient;
    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[]{".jpg", ".jpeg", ".png"};
    public UpLoadResult upload(MultipartFile multipartFile){
        // 校验图片格式
        boolean  isLegal = false;
        for (String type:IMAGE_TYPE){
            if(StringUtils.endsWithIgnoreCase(multipartFile.getOriginalFilename(),type)){
                isLegal = true;
                break;
            }
        }
        //图片大小限制5M
        try {
            if((multipartFile.getBytes().length/1024/1024)>5){
                isLegal = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        UpLoadResult upLoadResult = new UpLoadResult();
        if (!isLegal){
            upLoadResult.setStatus("error");
            return  upLoadResult;
        }
        String fileName = multipartFile.getOriginalFilename();
        String filePath = getFilePath(fileName);
        try {
            ossClient.putObject(aliyunConfig.getBucketName(),filePath,new ByteArrayInputStream(multipartFile.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            // 上传失败
            upLoadResult.setStatus("error");
            return  upLoadResult;
        }
        upLoadResult.setStatus("done");
        upLoadResult.setName(aliyunConfig.getUrlPrefix()+filePath);
        upLoadResult.setUid(filePath);
        return  upLoadResult;
    }
    // 生成不重复的文件路径和文件名
    private String getFilePath(String sourceFileName) {
        DateTime dateTime = new DateTime();
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + UUID.randomUUID().toString() + "." +
                sourceFileName.substring(sourceFileName.lastIndexOf("."));
    }

    public void download(String fileUrl){
        ossClient.getObject(new GetObjectRequest(aliyunConfig.getBucketName(), fileUrl), new File("/a.png"));
    }
    public void delete(String fileUrl){
        ossClient.deleteObject(new GetObjectRequest(aliyunConfig.getBucketName(), fileUrl));
    }
}
