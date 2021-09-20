package com.community.life.enums;

public enum NotificationEnum {
    REPLY_QUESTION(1, "回复了问题"),
    REPLY_COMMENT(2, "回复了评论");


    private int status;
    private String name;

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    NotificationEnum(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public static String nameOfType(int type){
        for (NotificationEnum value : NotificationEnum.values()) {
            if (value.getStatus() == type){
                return value.getName();
            }
        }
        return "";
    }
}
