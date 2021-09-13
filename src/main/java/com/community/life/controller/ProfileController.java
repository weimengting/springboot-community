package com.community.life.controller;


import com.community.life.bean.User;
import com.community.life.dto.PageDto;
import com.community.life.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")  //通过动作来路由到新的地址
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action, //默认从第一页开始显示，每页显示5条数据
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size,
                          Model model){
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user.toString());
        if (user == null){
            return "redirect:/";
        }

        if (action.equals("questions")){
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的问题");
        }
        else if (action.equals("replies")){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }

        PageDto pageDto = questionService.listByUserId(user.getId(), page, size);
        model.addAttribute("pageDto", pageDto);

        return "profile";
    }
}
