package com.community.life.controller;

import com.community.life.bean.Question;
import com.community.life.bean.User;
import com.community.life.mapper.QuestionMapper;
import com.community.life.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


@Controller
public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        System.out.println("coming here...");
        return "publish";
    }

    @PostMapping("/publish") //model是用来与前端进行交互的
    public String doPublish(@RequestParam("title") String title, //接收网页传递过来的参数
                            @RequestParam("tag") String tag,
                            @RequestParam("description") String description,
                            HttpServletRequest request,
                            Model model){
        System.out.println("coming to postmapping...");

        model.addAttribute("title", title);
        model.addAttribute("tag", tag);
        model.addAttribute("description", description);
        if (title == null || title.equals("")){
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description.equals("")){
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (tag == null || tag.equals("")){
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        //需要注意的是，此时已经创建了一个user对象，user ！= null始终成立，想要判断是否获得真正的user应该判断user.getName() == null
        User user = new User();
        //向浏览器请求该用户的cookie，获取相应的token信息，根据token向数据库中查询相应的user信息
        Cookie[] cookies = request.getCookies();
        //考虑到cookie可能不存在的情况
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String value = cookie.getValue();
                    user = userMapper.findByToken(value);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                        System.out.println(user.getName());
                    }
                    break;
                }
            }
        }
        if (user.getName() == null){
            model.addAttribute("error", "用户未登录");
            return "publish";
        }

        Question question = new Question();
        question.setTag(tag);
        question.setTitle(title);
        question.setDescription(description);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(System.currentTimeMillis());

        questionMapper.insert(question);

        return "redirect:/";
    }
}
