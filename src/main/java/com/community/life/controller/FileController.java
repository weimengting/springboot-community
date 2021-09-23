package com.community.life.controller;

import com.community.life.dto.FileDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {

    @ResponseBody
    @RequestMapping("/file/upload")
    public FileDto upload(){
        FileDto fileDto = new FileDto();
        fileDto.setSuccess(1);
        //如何将本地上传的文件传送到服务器
        fileDto.setUrl("/imgs/push.png");
        return fileDto;
    }
}
