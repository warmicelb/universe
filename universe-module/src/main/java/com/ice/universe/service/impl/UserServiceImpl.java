package com.ice.universe.service.impl;

import com.ice.universe.domain.User;
import com.ice.universe.repository.UserMapper;
import com.ice.universe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author liubin
 * @Date 2019/5/31 2:20 PM
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
