package com.hjg.service;

import com.hjg.bean.User;

import java.util.List;

public interface UserService {
    void save(User user);
    boolean deleteById(int uid);
    User findByUid(int uid);
    User findByUsername(String username);
    List<User> findByStatus(int status);
    List<User> findAll();
}
