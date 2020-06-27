package com.jin.demo.fastdfs.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

/**
 * @author wangjin
 */
@Component
public class FastDfsClientService {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    public String  uploadFile(byte[] bytes,long fileSize,String extension){
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        StorePath storePath = fastFileStorageClient.uploadFile(byteArrayInputStream,fileSize,extension,null);
        System.out.println(storePath.getGroup() + ":" + storePath.getPath() + ":"+storePath.getFullPath());
        return  storePath.getFullPath();
    }
    public  byte[] downloadFile(String fileUrl){
        String group = fileUrl.substring(0,fileUrl.indexOf("/"));
        String path = fileUrl.substring(fileUrl.indexOf("/")+1);
        DownloadByteArray downloadByteArray = new DownloadByteArray();
        byte[] bytes  = fastFileStorageClient.downloadFile(group,path,downloadByteArray);
        return   bytes;
    }
}
