package com.redistemplate.stream.demo.common;

import lombok.Data;

@Data
public class ResponseEntity<T> {
    private String msg;
    private Integer code;
    private T data;

    public static<T> ResponseEntity<T> success(T data){
        ResponseEntity responseEntity =new ResponseEntity();
        responseEntity.setData(data);
        responseEntity.setCode(0);
        responseEntity.setMsg("success");
        return responseEntity;
    }

    public static ResponseEntity error(String msg, Integer code){
        ResponseEntity responseEntity =new ResponseEntity();
        responseEntity.setCode(code);
        responseEntity.setMsg(msg);
        return responseEntity;
    }

    public static ResponseEntity error(ErrorEnums errorEnums){
        return error(errorEnums.getMsg(),errorEnums.getCode());
    }

    public static ResponseEntity error(BaseException e){
        return error(e.getMsg(),e.getCode());
    }

}
