package com.commenau.model;

public enum LogLevel {
    INFO(0), WARNING(1), DANGER(2);
    private final int value;
    LogLevel(int i) {
        this.value = i;
    }
    public int getValue(){
        return value;
    }

}
