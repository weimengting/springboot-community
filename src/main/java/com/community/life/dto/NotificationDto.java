package com.community.life.dto;

import com.community.life.bean.User;
import lombok.Data;

@Data
public class NotificationDto {

    private Integer id;
    private Long gmtCreate;
    private Integer status;
    private Integer notifier;   //通知人Id
    private String notifierName;
    private String outerTitle;
    private Integer outerId;
    private String typeName;   //通知回复的类型名称
    private Integer type;  //通知回复的类型编号
}
