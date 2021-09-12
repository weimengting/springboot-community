package com.community.life.controller;

import com.community.life.bean.User;
import com.community.life.dto.PageDto;
import com.community.life.mapper.QuestionMapper;
import com.community.life.mapper.UserMapper;
import com.community.life.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/") //该注解确保收到http转到/的请求时，能够转到该方法上
    public String hello(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size){
        //@RequestParam接收来自地址绑定的参数
        //一开始访问该页面时，先去拿到cookie中的token去数据库中验证，可以省略登陆的步骤
        //只适合小数据量的用户访问
        Cookie[] cookies = request.getCookies();
        //考虑到cookie可能不存在的情况
        if (cookies != null && cookies.length != 0){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String value = cookie.getValue();
                    User user = userMapper.findByToken(value); //看看能不能找到该token对应的user，找不到就返回首页，找到则显示登录态
                    if (user != null){
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        System.out.println("page = " + page);
        PageDto pageDto = questionService.list(page, size);
        model.addAttribute("pageDto", pageDto);
        return "index";   //自动去资源的template里面寻找名字为“hello”的html文件,将该文件渲染成页面
    }
}
