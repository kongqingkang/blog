package com.kqk.blog.dao;

import com.kqk.blog.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @auhtor kqk
 * @date 2019/11/13 0013 - 11:00
 * Repository(仓库)
 */
public interface TypeRepository extends JpaRepository<Type, Long> {
    Type findByName(String name);

    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);
}
