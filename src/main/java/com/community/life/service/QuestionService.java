package com.community.life.service;
//service是中间层
import com.community.life.bean.Question;
import com.community.life.bean.QuestionExample;
import com.community.life.bean.User;
import com.community.life.dto.PageDto;
import com.community.life.dto.QuestionDto;
import com.community.life.dto.QuestionQueryDto;
import com.community.life.exception.CustomizeErrorCode;
import com.community.life.exception.CustomizeException;
import com.community.life.mapper.QuestionExtMapper;
import com.community.life.mapper.QuestionMapper;
import com.community.life.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    //首页滚动的问题列表
    public PageDto list(String search, Integer page, Integer size){
        if (search != null){
            System.out.println("search内容的长度：" + search.length());
        }

        if (StringUtils.isNotBlank(search) && search.length() > 0){
            String[] tags = StringUtils.split(search, " ");
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }
        else {
            search = null;
        }

        QuestionQueryDto questionQueryDto = new QuestionQueryDto();
        questionQueryDto.setSearch(search);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDto);
        if (totalCount == 0){
            return new PageDto<QuestionDto>();
        }

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
        //从第几条开始的多少条记录
//        QuestionExample questionExample = new QuestionExample();
//        questionExample.setOrderByClause("gmtCreate desc");

        questionQueryDto.setPage(offset);
        questionQueryDto.setSize(size);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDto);

//        List<Question> questions =
//                questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
        List<QuestionDto> questionDtos = new ArrayList<>();
        PageDto<QuestionDto> pageDto = new PageDto<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);  //快速的把一个对象的所有属性迁移到另一个对象中
            questionDto.setUser(user);
            questionDtos.add(questionDto);
        }
        pageDto.setData(questionDtos);
        pageDto.setPageDto(page, totalPage);
        return pageDto;
    }

    //在我的问题页面，分页显示我的问题(根据userid查询所有的我提问的问题，并进行分页显示)
    public PageDto listByUserId(Integer userId, Integer page, Integer size){
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);

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

        //返回满足条件的所有question对象
        QuestionExample questionExample1 = new QuestionExample();
        questionExample1.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions =
                questionMapper.selectByExampleWithRowbounds(questionExample1, new RowBounds(offset, size));
        List<QuestionDto> questionDtos = new ArrayList<>();
        PageDto<QuestionDto> pageDto = new PageDto<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);  //快速的把一个对象的所有属性迁移到另一个对象中
            questionDto.setUser(user);
            questionDtos.add(questionDto);
        }
        pageDto.setData(questionDtos);
        pageDto.setPageDto(page, totalPage);
        return pageDto;
    }


    //根据问题的id返回相应的问题
    public QuestionDto getById(Integer id){
        System.out.println(id);
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question, questionDto);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDto.setUser(user);
        return questionDto;
    }


    //检查问题是否已经创建，如果已创建，则更新；否则新建
    public void createOrUpdate(Question question){
        //判断问题是否是第一次创建（是的话id为空，存放到数据库之后才会有id）
        if (question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(System.currentTimeMillis());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        }
        else {
            Question updateQuestion = new Question();
            //只将需要更新的内容写入即可，update执行时只会更新里面存在的属性
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updated == 0){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Integer id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    //根据标签内容获取相关问题的列表
    public List<QuestionDto> selectRelated(QuestionDto questionDto) {
        if (StringUtils.isBlank(questionDto.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(questionDto.getTag(), ",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(questionDto.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDto> res = questions.stream().map(q -> {
            QuestionDto questionDtoTemp = new QuestionDto();
            BeanUtils.copyProperties(q, questionDtoTemp);
            return questionDtoTemp;
        }).collect(Collectors.toList());

        return res;
    }
}
