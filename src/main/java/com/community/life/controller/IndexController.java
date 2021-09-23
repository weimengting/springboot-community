package com.community.life.controller;

import com.community.life.dto.PageDto;
import com.community.life.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/") //该注解确保收到http转到/的请求时，能够转到该方法上
    public String hello(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size,
                        @RequestParam(name = "search", required = false) String search){
        //@RequestParam接收来自地址绑定的参数
        //一开始访问该页面时，先去拿到cookie中的token去数据库中验证，可以省略登陆的步骤
        //只适合小数据量的用户访问
        PageDto pageDto = questionService.list(search, page, size);

        if (pageDto.getData() == null && pageDto.getTotalPage() == null){
            model.addAttribute("pageDto", pageDto);
            return "index";
        }
        model.addAttribute("pageDto", pageDto);
        model.addAttribute("searchch", search);
        return "index";   //自动去资源的template里面寻找名字为“hello”的html文件,将该文件渲染成页面
    }
}
