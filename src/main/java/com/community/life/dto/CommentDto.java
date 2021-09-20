package com.community.life.dto;


import com.community.life.bean.User;
import lombok.Data;

//服务器将资源返回给客户端的时候的数据（将object类型用json封装）

@Data
public class CommentDto {
    private Integer id;
    private Integer parentId;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private User user;
    private Integer commentCount;
}
