package com.wang.blog.service;

import com.wang.blog.pojo.User;
import org.springframework.stereotype.Service;


public interface UserService {
    User checkUser(String username,String password);

}
