package com.community.life.exception;

//定义在程序执行过程中出现的异常

public class CustomizeException extends RuntimeException {

    private String message;
    private Integer code;


    public CustomizeException(ICustomizeErrorCode customizeErrorCode){
        this.code = customizeErrorCode.getCode();
        this.message = customizeErrorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
