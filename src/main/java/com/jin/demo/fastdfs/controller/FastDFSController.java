package com.jin.demo.fastdfs.controller;

import com.jin.demo.fastdfs.service.FastDfsClientService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author wangjin
 */
@RestController
@RequestMapping("/fastdfs")
public class FastDFSController {
    @Autowired
    private FastDfsClientService fastDfsClientService;
    @RequestMapping("/upload")
    public String uploadFile(MultipartFile file)throws IOException {
        byte[] bytes  = file.getBytes();
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")+1);
        String fileName = file.getName();
        long  fileSize = file.getSize();
        System.out.println(originalFileName+":"+fileName+":"+fileSize +":"+extension);

        return  fastDfsClientService.uploadFile(bytes,fileSize,extension);
    }
    @RequestMapping("/download")
    public void downloadFile(String fileUrl, HttpServletResponse response)throws  IOException{
        byte[] bytes = fastDfsClientService.downloadFile(fileUrl);
        //生成缩略图
        byte[] bytes1 = toSmall(bytes);
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileUrl, "UTF-8"));
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(bytes1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] toSmall(byte[] bytes) throws IOException {
        File from = getFile(bytes, "/", "1.png");
        File to = new File("/2.png");
        Thumbnails.of(from).scale(0.2D).toFile(to);
        try (FileInputStream fis = new FileInputStream(to)){

            return fis.readAllBytes();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**根据byte[] 数组生成文件  （在本地）
     * @param bfile  字节数组
     * @param filePath 文件路径
     * @param fileName 文件名
     */
    public File getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;  //带缓冲得文件输出流
        FileOutputStream fos = null;      //文件输出流
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath+fileName);  //文件路径+文件名
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

}
