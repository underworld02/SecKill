package com.suron.common.exception;

/**
 * @author ysy
 * @version 1.0
 */
public enum HsplivingCodeEnum {

    //如果小伙伴忘记枚举怎么使用，可以到java基础回顾
    UNKNOWN_EXCEPTION(40000,"系统未知异常"),
    INVALID_EXCEPTION(40001,"参数校验异常");

    private int code;
    private String msg;

    HsplivingCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
