package com.redistemplate.stream.demo.common;

import lombok.Data;

@Data
public class Result<T> {
    private String msg;
    private Integer code;
    private T data;

    public static<T> Result<T> success(T data){
        Result result=new Result();
        result.setData(data);
        result.setCode(0);
        result.setMsg("success");
        return result;
    }

    public static Result error(String msg,Integer code){
        Result result=new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result error(ErrorEnums errorEnums){
        return error(errorEnums.getMsg(),errorEnums.getCode());
    }

}
