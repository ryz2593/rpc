package com.ryz2593.rpc.service;

import com.ryz2593.rpc.api.UserService;

/**
 * @author ryz2593
 * @date 2019/4/19
 * @desc
 */
public class UserServiceImpl implements UserService {
    @Override
    public String addUser(String name) {
        return "add User name = " + name;
    }
}
