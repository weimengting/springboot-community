package com.community.life.mapper;

import com.community.life.bean.User;
import org.springframework.stereotype.Repository;


@Repository
public class UserMapperImpl implements UserMapper {
    @Override
    public void insert(User user) {

    }

    @Override
    public User findByToken(String token) {
        return null;
    }

    @Override
    public User findById(Integer id) {
        return null;
    }

}
