package com.community.life.dto;

//进行github授权(当参数超过5个时，使用对象的方式来进行传递)

import lombok.Data;

@Data
public class AccessTokenDto {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
}
