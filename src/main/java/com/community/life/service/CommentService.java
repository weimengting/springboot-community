package com.community.life.service;


import com.community.life.bean.*;
import com.community.life.dto.CommentDto;
import com.community.life.enums.CommentTypeEnum;
import com.community.life.enums.NotificationEnum;
import com.community.life.enums.NotificationStatusEnum;
import com.community.life.exception.CustomizeErrorCode;
import com.community.life.exception.CustomizeException;
import com.community.life.mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    //将对问题或者评论执行的回复内容存储到数据库中
    @Transactional
    public void insert(Comment comment, User commentator){

        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())){
            //回复评论的功能(在数据库里面找到应该回复的评论)
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);

            //增加对评论的评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incComment(parentComment);
            //创建通知
            createNotify(comment, dbComment.getCommentator(), commentator.getName(),
                    question.getTitle(), NotificationEnum.REPLY_COMMENT, question.getId());
        }
        else {
            //回复问题的功能
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incComment(question);

            //创建通知
            createNotify(comment, question.getCreator(), commentator.getName(),
                    question.getTitle(), NotificationEnum.REPLY_QUESTION, question.getId());
        }
    }

    //获取问题或者是评论的所有回复列表
    public List<CommentDto> listByTargetId(Integer id, CommentTypeEnum type) {
        CommentExample example = new CommentExample();
        example.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        example.setOrderByClause("gmtCreate desc");
        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments.isEmpty()){
            return new ArrayList<>();
        }
        //获取去重的评论人
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());

        //得到评论人对应的user信息，并转换成map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(new ArrayList<>(commentators));
        List<User> users = userMapper.selectByExample(userExample);
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //将评论人的user信息与评论相对应，添加到新的commentDto对象中
        List<CommentDto> commentDtos = comments.stream().map(comment -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(comment, commentDto);
            commentDto.setUser(userMap.get(comment.getCommentator()));
            return commentDto;
        }).collect(Collectors.toList());
        return commentDtos;
    }

    //无论什么情况下都跳转到question上面
    private void createNotify(Comment comment, Integer commentator, String notifierName,
                              String outerTitle, NotificationEnum type, Integer questionId){
        if (commentator.equals(comment.getCommentator())){
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setGmtModified(System.currentTimeMillis());
        notification.setType(type.getStatus());
        notification.setOuterId(questionId);
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(commentator);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }
}
