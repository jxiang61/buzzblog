package com.xjz.blog.service;

import com.xjz.blog.pojo.User;

public interface UserService {

    //login
    User checkUser(String username, String password);

    //register
    User findUser(String username);

    //save new user
    User saveUser(User user);
}
