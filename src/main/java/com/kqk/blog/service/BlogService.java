package com.kqk.blog.service;

import com.kqk.blog.po.Blog;
import com.kqk.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @auhtor kqk
 * @date 2019/11/14 0014 - 18:56
 */
public interface BlogService {
    Blog getBlog(Long id);

    Blog getAndConvert(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);//分页拿到博客列表对象，包括了查询

    Page<Blog> listBlog(Pageable pageable);//分页拿到博客列表对象

    Page<Blog> listBlog(Long tagId, Pageable pageable);//通过id和分页拿到博客列表对象

    Page<Blog> listBlog(String query, Pageable pageable);//分页拿到博客列表对象,首页查询

    List<Blog> listRecommendBlogTop(Integer size);

    Blog saveBlog(Blog blog);

    Map<String, List<Blog>> archiveBlog();

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);

    Long countBlog();//拿到条数
}
