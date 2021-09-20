package com.community.life.controller;


import com.community.life.dto.CommentDto;
import com.community.life.dto.QuestionDto;
import com.community.life.enums.CommentTypeEnum;
import com.community.life.service.CommentService;
import com.community.life.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id,
                           Model model){

        //根据id获取该问题相关的一系列信息
        QuestionDto questionDto = questionService.getById(id);
        //根据该问题的标签索引可能相关的所有问题
        List<QuestionDto> relatedQuestions = questionService.selectRelated(questionDto);
        //根据该问题的id获取该问题下面的所有回复内容
        List<CommentDto> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);

        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question", questionDto);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }
}
