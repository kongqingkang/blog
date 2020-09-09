package com.kqk.blog.service;

import com.kqk.blog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @auhtor kqk
 * @date 2019/11/14 0014 - 12:59
 */
public interface TagService {

    Tag saveTag(Tag tag);//保存

    Tag getTag(Long id);//获取

    Tag updateTag(Long id, Tag tag);//更新

    Page<Tag> listTag(Pageable pageable);//分页显示

    List<Tag> listTag();

    List<Tag> listTag(String ids);

    List<Tag> listTagToTop(Integer size);//首页类型列表的list

    void deleteTag(Long id);//删除

    Tag getTagByName(String name);//通过名称获取

}
