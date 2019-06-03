package com.ice.universe.common.enums;


import org.springframework.http.HttpStatus;

/**
 * @author: ice
 * @create: 2018/10/29
 **/
public interface ErrorCodeEnum extends MsgCodeEnum {

    HttpStatus getHttpStatus();
}
