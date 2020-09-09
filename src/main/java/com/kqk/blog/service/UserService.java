package com.kqk.blog.service;

import com.kqk.blog.po.User;

/**
 * @auhtor kqk
 * @date 2019/11/12 0012 - 18:58
 */
public interface UserService {
    User checkUser(String username, String password);
}
