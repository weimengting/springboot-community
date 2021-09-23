package com.community.life.mapper;

import com.community.life.bean.Question;
import com.community.life.dto.QuestionQueryDto;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);

    int incComment(Question record);

    List<Question> selectRelated(Question record);

    Integer countBySearch(QuestionQueryDto questionQueryDto);

    List<Question> selectBySearch(QuestionQueryDto questionQueryDto);

}