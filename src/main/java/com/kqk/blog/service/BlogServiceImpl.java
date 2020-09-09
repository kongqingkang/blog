package com.kqk.blog.service;

import com.kqk.blog.NotFoundException;
import com.kqk.blog.dao.BlogRepoistory;
import com.kqk.blog.po.Blog;
import com.kqk.blog.po.Type;
import com.kqk.blog.util.MarkdownUtils;
import com.kqk.blog.util.MyBeanUtils;
import com.kqk.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @auhtor kqk
 * @date 2019/11/14 0014 - 18:59
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepoistory blogRepoistory;

    @Transactional
    @Override
    public Blog getBlog(Long id) {
        return blogRepoistory.getOne(id);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepoistory.getOne(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在！");
        }
        //下面的两行代码是为了不让保存到数据库里的值变成html
        //用BeanUtils.copyProperties方法复制一个b，通过对复制的对象进行操作
        //这样就不会改变数据库中的值
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        blog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogRepoistory.updateViews(id);
        return b;
    }

    /**
     * 前端首页拿到list
     *
     * @param pageable
     * @return
     */
    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepoistory.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepoistory.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepoistory.findByQuery(query, pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepoistory.findTop(pageable);
    }

    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {

        return blogRepoistory.findAll(new Specification<Blog>() {
            @Override
            //动态查询，Root<Blog> root表示要查询的对象
            //CriteriaQuery<?> query表示查询的容器
            //CriteriaBuilder criteriaBuilder表示具体查询的表达式，如“=”
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogRepoistory.save(blog);
    }

    @Transactional
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepoistory.findGroupYears();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepoistory.findByYear(year));
        }
        return map;
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog blog1 = blogRepoistory.getOne(id);
        if (blog1 == null) {
            throw new NotFoundException("此博客不存在呢!");
        }
        BeanUtils.copyProperties(blog, blog1, MyBeanUtils.getNullPropertyNames(blog));
        blog1.setUpdateTime(new Date());
        return blogRepoistory.save(blog1);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepoistory.deleteById(id);
    }

    @Transactional
    @Override
    public Long countBlog() {
        return blogRepoistory.count();
    }
}
