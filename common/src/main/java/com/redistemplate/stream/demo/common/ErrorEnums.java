package com.redistemplate.stream.demo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnums {
    CONSUMER_NAME_NOT_NULL(-1001,"流名字不能为空"),
    PARAM_ERROR(-1002,"参数错误");
    private Integer code;
    private String msg;
}
