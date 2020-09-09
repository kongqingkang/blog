package com.kqk.blog.service;

import com.kqk.blog.po.Comment;

import java.util.List;

/**
 * @auhtor kqk
 * @date 2019/11/25 0025 - 19:50
 */
public interface CommentService {

    //获取评论列表
    List<Comment> listCommentByBlogId(Long blogId);

    //保存评论信息
    Comment saveComment(Comment comment);
}
