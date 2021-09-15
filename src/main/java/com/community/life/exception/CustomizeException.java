package com.community.life.exception;

//定义在程序执行过程中出现的异常

public class CustomizeException extends RuntimeException {

    private String message;

    public CustomizeException(String message) {
        this.message = message;
    }

    public CustomizeException(ICustomizeErrorCode customizeErrorCode){
        this.message = customizeErrorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
