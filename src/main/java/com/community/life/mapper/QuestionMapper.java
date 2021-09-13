package com.community.life.mapper;

import com.community.life.bean.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Insert("insert into question values(null,#{title},#{description}," +
            "#{gmtCreate},#{gmtModified},#{creator},#{commentCount},#{viewCount},#{likeCount},#{tag})")
    void insert(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> listAll(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);  //传输非对象的时候需要使用param自己做一个映射

    @Select("select count(1) from question")
    Integer countNum();

    @Select("select * from question where creator=#{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param(value = "userId") Integer userId,
                        @Param(value = "offset") Integer offset,
                        @Param(value = "size") Integer size);

    @Select("select count(1) from question where creator=#{userId}")
    Integer countNumByUserId(@Param(value = "userId") Integer userId);
}
