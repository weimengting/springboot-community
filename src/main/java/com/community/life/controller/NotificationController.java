package com.community.life.controller;

import com.community.life.bean.User;
import com.community.life.dto.NotificationDto;
import com.community.life.enums.NotificationEnum;
import com.community.life.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")  //通过动作来路由到新的地址
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Integer id){  //这里的id是notification在数据库中对应的id
        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            return "redirect:/";
        }
        //对用户的身份做校验，检索该问题是否为该用户提出
        NotificationDto notificationDto = notificationService.read(id, user);
        if (NotificationEnum.REPLY_COMMENT.getStatus() == notificationDto.getType() ||
                NotificationEnum.REPLY_QUESTION.getStatus() == notificationDto.getType()){
            //如果是对评论或问题进行回复
            return "redirect:/question/" + notificationDto.getOuterId();
        }
        return "redirect:/";
    }
}
