package com.community.life.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NOT_FOUND(2001,"需要寻找的问题不存在，要不要换个试试？"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题进行评论或回复"),
    NO_LOGIN(2003, "当前操作需要登录，请登陆后重试"),
    SYSTEM_ERROR(2004, "服务器已崩溃，请稍后再来试试~~~"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "评论不存在"),
    CONTENT_IS_EMPTY(2007, "输入内容不能为空"),
    READ_NOTIFICATION_FAIL(2008, "无法读取其他用户的信息"),
    NOTIFICATION_NOT_FOUND(2009, "消息没有了，请重试");


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) { //添加一个有参构造是为了在外部调用时将信息传入
        this.message = message;
        this.code = code;
    }
}
