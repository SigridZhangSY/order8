package com.thoughtworks.ketsu.infrastructure.repositories;

import com.thoughtworks.ketsu.domain.user.User;
import com.thoughtworks.ketsu.infrastructure.mybatis.mappers.UserMapper;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

public class UserRepository implements com.thoughtworks.ketsu.domain.user.UserRepository {

    @Inject
    UserMapper userMapper;
    @Override
    public Optional<User> createUser(Map<String, Object> info) {

        userMapper.save(info);
        return Optional.ofNullable(userMapper.findById(Long.valueOf(String.valueOf(info.get("id")))));
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(userMapper.findById(id));
    }
}
