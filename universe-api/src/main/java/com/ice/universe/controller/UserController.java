package com.ice.universe.controller;

import com.ice.universe.domain.User;
import com.ice.universe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author liubin
 * @Date 2019/5/31 2:40 PM
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/{userId}")
    public User user(@PathVariable Long userId){
        return userService.findUserById(userId);
    }

    @RequestMapping(value = "/test")
    public User test(){
        System.out.println("aa");
        return userService.findUserById(1L);
    }
}
