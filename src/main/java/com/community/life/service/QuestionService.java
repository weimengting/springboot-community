package com.community.life.service;
//service是中间层
import com.community.life.bean.Question;
import com.community.life.bean.User;
import com.community.life.dto.PageDto;
import com.community.life.dto.QuestionDto;
import com.community.life.mapper.QuestionMapper;
import com.community.life.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    //首页滚动的问题列表
    public PageDto list(Integer page, Integer size){
        Integer totalCount = questionMapper.countNum();
        Integer totalPage;
        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }
        else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1){
            page = 1;
        }
        if (page > totalPage){
            page = totalPage;
        }
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.listAll(offset, size);
        List<QuestionDto> questionDtos = new ArrayList<>();
        PageDto pageDto = new PageDto();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);  //快速的把一个对象的所有属性迁移到另一个对象中
            questionDto.setUser(user);
            questionDtos.add(questionDto);
        }
        pageDto.setQuestionDtos(questionDtos);
        pageDto.setPageDto(page, totalPage);
        return pageDto;
    }

    //在我的问题页面，分页显示我的问题
    public PageDto listByUserId(Integer userId, Integer page, Integer size){
        Integer totalCount = questionMapper.countNumByUserId(userId);
        System.out.println(totalCount);
        Integer totalPage;
        if (totalCount % size == 0){
            totalPage = totalCount / size;
        }
        else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1){
            page = 1;
        }
        if (page > totalPage){
            page = totalPage;
        }
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.listByUserId(userId, offset, size);  //返回满足条件的所有question对象
        List<QuestionDto> questionDtos = new ArrayList<>();
        PageDto pageDto = new PageDto();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);  //快速的把一个对象的所有属性迁移到另一个对象中
            questionDto.setUser(user);
            questionDtos.add(questionDto);
        }
        pageDto.setQuestionDtos(questionDtos);
        pageDto.setPageDto(page, totalPage);
        return pageDto;
    }
}
