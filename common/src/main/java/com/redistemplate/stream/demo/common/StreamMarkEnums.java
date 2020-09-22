package com.redistemplate.stream.demo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author lulu
 * @Date 2020/9/21 22:09
 */
@Getter
@AllArgsConstructor
public enum StreamMarkEnums {
    START(0, "0"),
    LAST(1, ">"),
    MIN_ID(-1, "-"),
    MAX_ID(2, "+");
    private Integer code;
    private String value;
}
