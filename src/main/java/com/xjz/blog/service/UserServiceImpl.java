package com.xjz.blog.service;

import com.xjz.blog.dao.UserRepository;
import com.xjz.blog.pojo.User;
import com.xjz.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {

        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));

        return user;
    }

    @Override
    public User findUser(String username) {
        User user = userRepository.findByUsername(username);

        return user;
    }

    @Override
    public User saveUser(User user) {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setNickname(user.getUsername());
        user.setPassword(MD5Utils.code(user.getPassword()));
        return userRepository.save(user);
    }
}
