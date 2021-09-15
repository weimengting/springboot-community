package com.community.life.controller;

import com.community.life.bean.Question;
import com.community.life.bean.User;
import com.community.life.dto.QuestionDto;
import com.community.life.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model){
        //需要说明一点，此处的id不需要通过model来装载就能传递到前端
        QuestionDto questionDto = questionService.getById(id);
        model.addAttribute("title", questionDto.getTitle());
        model.addAttribute("tag", questionDto.getTag());
        model.addAttribute("description", questionDto.getDescription());
        return "publish";
    }


    @GetMapping("/publish")
    public String publish(){
        System.out.println("coming here...");
        return "publish";
    }

    @PostMapping("/publish") //model是用来与前端进行交互的
    public String doPublish(@RequestParam(value = "title", required = false) String title, //接收网页传递过来的参数
                            @RequestParam(value = "tag", required = false) String tag,
                            @RequestParam(value = "description", required = false) String description,
                            @RequestParam(value = "id", required = false) Integer id,
                            HttpServletRequest request,
                            Model model){

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
        //需要注意的是，如果此时已经创建了一个user对象，user ！= null始终成立，想要判断是否获得真正的user应该判断user.getName() == null
        User user = (User) request.getSession().getAttribute("user");
        //向浏览器请求该用户的cookie，获取相应的token信息，根据token向数据库中查询相应的user信息

        if (user == null){
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        System.out.println("coming to publishController----/publish");
        Question question = new Question();
        question.setTag(tag);
        question.setTitle(title);
        question.setDescription(description);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
