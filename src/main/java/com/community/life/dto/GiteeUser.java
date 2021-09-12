package com.community.life.dto;


import lombok.Data;

@Data
public class GiteeUser { //实际上是GiteeProvider的一个返回值

    private String name;
    private Long id;
    private String bio;
    private String avatar_url;  //指向用户头像图片的链接
}
