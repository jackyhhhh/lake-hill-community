package com.hjg.service.impl;

import com.hjg.bean.User;
import com.hjg.repository.UserRepository;
import com.hjg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    @Override
    public void save(User user) {
        repo.save(user);
    }

    @Override
    public boolean deleteById(int uid) {
        if(repo.existsById(uid)){
            repo.deleteById(uid);
            return true;
        }
        return false;
    }

    @Override
    public User findByUid(int uid) {
        if (repo.existsById(uid)) {
            Optional<User> optional = repo.findById(uid);
            if(optional.isPresent()){
                return optional.get();
            }
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return repo.findByUsername(username);
    }

    @Override
    public List<User> findByStatus(int status) {
        return repo.findByStatus(status);
    }

    @Override
    public List<User> findAll() {
        return repo.findAll();
    }
}
