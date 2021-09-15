package com.community.life.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NOT_FOUND("需要寻找的问题不存在，要不要换个试试？");

    @Override
    public String getMessage() {
        return message;
    }

    private String message;

    CustomizeErrorCode(String message){ //添加一个有参构造是为了在外部调用时将信息传入
        this.message = message;
    }
}
