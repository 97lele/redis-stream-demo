package com.redistemplate.stream.demo.common;

/**
 * @author lele
 * 基础异常类
 */
public class BaseException extends RuntimeException{
    private String msg;
    private Integer code;
    public BaseException(Integer code,String msg){
        super(msg);
        this.code=code;
    }
    public BaseException(ErrorEnums errorEnums){
        this(errorEnums.getCode(),errorEnums.getMsg());
    }

}
