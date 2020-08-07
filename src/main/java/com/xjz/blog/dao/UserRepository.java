package com.xjz.blog.dao;

import com.xjz.blog.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

//extends CURD methods
//then we can use those in service
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(String username, String password);

    User findByUsername(String username);
}
