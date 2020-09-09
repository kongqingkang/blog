package com.kqk.blog.web.admin;

import com.kqk.blog.po.Blog;
import com.kqk.blog.po.User;
import com.kqk.blog.service.BlogService;
import com.kqk.blog.service.TagService;
import com.kqk.blog.service.TypeService;
import com.kqk.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


/**
 * @auhtor kqk
 * @date 2019/11/12 0012 - 20:31
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;

    /**
     * 分页获得记录
     *
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return LIST;
    }

    /**
     * 搜索并按分页返回结果
     *
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs::blogList";
    }

    /**
     * 设置tag和type
     *
     * @param model
     */
    private void setTagAndType(Model model) {
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("types", typeService.listType());
    }

    /**
     * 新增页面
     *
     * @param model
     * @return
     */
    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTagAndType(model);
        model.addAttribute("blog", new Blog());//修改初始化的时候页面拿到值
        return INPUT;
    }

    /**
     * 编辑页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTagAndType(model);
        model.addAttribute("blog", blogService.getBlog(id));//修改初始化的时候页面拿到值
        Blog blog = blogService.getBlog(id);
        blog.init();
        return INPUT;
    }

    /**
     * 新增页面的提交
     *
     * @param blog
     * @param attributes
     * @param session
     * @return
     */
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId() == null) {
            b = blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }
        if (b == null) {
            attributes.addFlashAttribute("message", "操作失败！");
        } else {
            attributes.addFlashAttribute("message", "操作成功！");
        }
        return REDIRECT_LIST;
    }


    /**
     * 删除记录
     *
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功！");
        return REDIRECT_LIST;
    }
}
