package com.community.life.controller;


import com.community.life.bean.User;
import com.community.life.dto.NotificationDto;
import com.community.life.dto.PageDto;
import com.community.life.service.NotificationService;
import com.community.life.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")  //通过动作来路由到新的地址
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action, //默认从第一页开始显示，每页显示5条数据
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size,
                          Model model){
        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            return "redirect:/";
        }

        if ("questions".equals(action)){
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的问题");
            PageDto pageDto = questionService.listByUserId(user.getId(), page, size);
            model.addAttribute("pageDto", pageDto);
        }
        else if ("replies".equals(action)){
            //展示当前页面的通知
            PageDto pageDto = notificationService.list(user.getId(), page, size);
            Long num = notificationService.unreadCount(user.getId());
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            model.addAttribute("pageDto", pageDto);
            model.addAttribute("unreadCount", num);
        }



        return "profile";
    }
}
