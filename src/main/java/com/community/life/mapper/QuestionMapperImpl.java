package com.community.life.mapper;

import com.community.life.bean.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionMapperImpl implements QuestionMapper {
    @Override
    public void insert(Question question) {

    }

    @Override
    public List<Question> listAll(Integer offset, Integer size) {
        return null;
    }

    @Override
    public Integer countNum() {
        return null;
    }
}
