package com.redistemplate.stream.demo.console.exceptions;

import com.redistemplate.stream.demo.common.BaseException;
import com.redistemplate.stream.demo.common.ErrorEnums;

/**
 * @author lele
 */
public class ConsoleException extends BaseException {
    public ConsoleException(Integer code, String msg) {
        super(code, msg);
    }

    public ConsoleException(ErrorEnums errorEnums) {
        super(errorEnums);
    }
}
