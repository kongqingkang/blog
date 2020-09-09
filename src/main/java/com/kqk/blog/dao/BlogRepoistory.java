package com.kqk.blog.dao;

import com.kqk.blog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @auhtor kqk
 * @date 2019/11/14 0014 - 19:02
 */
public interface BlogRepoistory extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {
    @Query("select b from Blog b where b.recommend=true")
    List<Blog> findTop(Pageable pageable);

    @Query("select b from Blog b where b.title like ?1 or b.description like ?1")
        //如果有第二个参数就是?2
    Page<Blog> findByQuery(String query, Pageable pageable);

    @Modifying
    @Query("update Blog b set b.views=b.views+1 where b.id=?1")
    int updateViews(Long id);

    //属于JPA的语法
    //按照倒叙拿到所有的年份
    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by year desc")
    List<String> findGroupYears();

    //按照年份拿到所有的blog，形成博客列表
    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') =?1")
    List<Blog> findByYear(String year);
}
