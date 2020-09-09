package com.kqk.blog.dao;

import com.kqk.blog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @auhtor kqk
 * @date 2019/11/12 0012 - 19:04
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPassword(String username, String password);
}
