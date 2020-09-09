package com.kqk.blog.service;

import com.kqk.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @auhtor kqk
 * @date 2019/11/13 0013 - 10:31
 */
public interface TypeService {
    Type saveType(Type type);

    Type getType(Long id);

    Type geTypeByName(String name);

    Page<Type> listType(Pageable pageable);

    List<Type> listType();

    List<Type> listTypeToTop(Integer size);//首页类型列表的list

    Type updateType(Long id, Type type);

    void deleteType(Long id);

}
