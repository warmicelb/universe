package com.ice.universe.common.exception;

/**
 * @author: ice
 * @create: 2018/10/29
 **/
public class OpenException extends RuntimeException {

    public OpenException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenException(String message) {
        super(message);
    }
}
