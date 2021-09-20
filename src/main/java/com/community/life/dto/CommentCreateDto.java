package com.community.life.dto;


import lombok.Data;
//浏览器页面向服务器进行comment传输的专用类

@Data
public class CommentCreateDto {
    private String content;
    private Integer parentId;
    private Integer type;
}
