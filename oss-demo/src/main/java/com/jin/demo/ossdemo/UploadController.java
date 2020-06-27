package com.jin.demo.ossdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/pic")
public class UploadController {
    @Autowired
    private FileUpLoadService  fileUpLoadService;
    @PostMapping("/upload")
    @ResponseBody
    public UpLoadResult upload(@RequestParam("file") MultipartFile multipartFile){
        return  fileUpLoadService.upload(multipartFile);
    }

    @RequestMapping("/download")
    public void downloadFile(String fileUrl, HttpServletResponse response)throws IOException {
        fileUpLoadService.download(fileUrl);
        //获取字节流
        byte[] bytes1 = getBytes("/a.png");
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

    private byte[] getBytes(String s) throws IOException {
        return new FileInputStream(new File(s)).readAllBytes();
    }

    @RequestMapping("delete")
    @ResponseBody
    public String delete(String fileUrl){
        fileUpLoadService.delete(fileUrl);
        return "delete "+fileUrl+" success!!!";
    }
}
