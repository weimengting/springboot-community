package com.community.life.controller;

import com.community.life.bean.Comment;
import com.community.life.bean.User;
import com.community.life.dto.CommentCreateDto;
import com.community.life.dto.CommentDto;
import com.community.life.dto.ResultDto;
import com.community.life.enums.CommentTypeEnum;
import com.community.life.exception.CustomizeErrorCode;
import com.community.life.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    //@RequestBody可以自动地将传递过来的对象中的属性封装成json对象
    //@ResponseBody可以保证服务器返回的是一个json对象，而不是需要渲染的html页面
    //post方法请求向服务器中添加数据
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDto commentCreateDto,
                       HttpServletRequest request){
        //通过浏览器查看是否可以拿到user对象（即是否为登陆状态）
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            //静态的作用体现在这里，即不通过对象的实例化即可调用
            return ResultDto.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if (commentCreateDto == null || StringUtils.isBlank(commentCreateDto.getContent())){
            return ResultDto.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        //此处接收从浏览器界面传过来的评论内容，包括ParentId，type，content
        Comment comment = new Comment();
        comment.setParentId(commentCreateDto.getParentId());
        comment.setType(commentCreateDto.getType());
        comment.setContent(commentCreateDto.getContent());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);
        commentService.insert(comment, user);
        return ResultDto.okOf();
    }

    //get方法请求查询服务器端的数据，传过来的id是要评论的评论的id，类型为评论
    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDto<List> comments(@PathVariable(name = "id") Integer id){
        List<CommentDto> commentDtos = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);

        return ResultDto.okOf(commentDtos);
    }
}
