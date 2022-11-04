package com.wang.blog;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 指定一个返回的状态码
 * 它会把这个exception作为一个资源找不到的状态
 * springboot会拿到这个状态并对应到404页面
 * 后台返回HTTP响应的状态码为@ResponseStatus指定的状态码
 * @author wqy
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException() {

    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
