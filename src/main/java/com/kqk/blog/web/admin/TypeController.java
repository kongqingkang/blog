package com.kqk.blog.web.admin;

import com.kqk.blog.po.Type;
import com.kqk.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @auhtor kqk
 * @date 2019/11/13 0013 - 14:23
 * 属于web，页面的展示，具体会返回到哪个页面
 * import org.springframework.data.domain.Pageable
 */
@Controller
@RequestMapping("/admin")//全局的参数
public class TypeController {

    @Autowired
    //Autowired自动装配
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {//前端页面展示模型
        model.addAttribute("page", typeService.listType(pageable));//Attribute属性
        return "admin/types";//返回到的页面
    }

    @GetMapping("/types/input")//路径
    public String input(Model model) {
        model.addAttribute("type", new Type());
        return "admin/types-input";//真实的页面
    }

    @GetMapping("/types/{id}/input")//{id}的意思是要传id
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getType(id));
        //model.addAttribute()方法相当于map，前面是key，后面是value，前端可以拿到这个model视图，作用是传递数据
        return "admin/types-input";//真实的页面
    }

    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
        Type type1 = typeService.geTypeByName(type.getName());
        if (type1 != null) {
            result.rejectValue("name", "nameError", "该分类已存在！");
        }
        if (result.hasErrors()) {
            return "admin/types-input";
        }
        Type t = typeService.saveType(type);
        if (t == null) {
            attributes.addFlashAttribute("message", "新增失败！");
        } else {
            attributes.addFlashAttribute("message", "新增成功！");
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
        //BindingResult验证前端传递的参数，一定要跟在@Valid后面，中间不能隔开
        //@PathVariable映射 URL 绑定的占位符,这里指的是要带的参数id.
        Type type1 = typeService.geTypeByName(type.getName());
        if (type1 != null) {
            result.rejectValue("name", "nameError", "该分类已存在！");
        }
        if (result.hasErrors()) {
            return "admin/types-input";
        }
        Type t = typeService.updateType(id, type);
        if (t == null) {
            attributes.addFlashAttribute("message", "更新失败！");
        } else {
            attributes.addFlashAttribute("message", "更新成功！");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功！");
        return "redirect:/admin/types";
    }
}
