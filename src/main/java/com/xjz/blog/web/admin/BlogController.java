package com.xjz.blog.web.admin;

import com.xjz.blog.pojo.Blog;
import com.xjz.blog.pojo.User;
import com.xjz.blog.searchobject.BlogQuery;
import com.xjz.blog.service.BlogService;
import com.xjz.blog.service.TagService;
import com.xjz.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;


@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;




    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size=6, sort={"updateTime"}, direction= Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog,
                        Model model,
                        HttpSession session) {


        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("types", typeService.listType());
        model.addAttribute("user", currentUser);

        //takes in pageable and blog from frontend, then search
        blog.setUserId(currentUser.getId());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        //then frontend get the model, we can do some nice things

        return "admin/blogs";
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size=6, sort={"updateTime"}, direction= Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog,
                        HttpSession session,
                        Model model) {
        User currentUser = (User) session.getAttribute("user");

        //takes in pageable and blog from frontend, then search

        blog.setUserId(currentUser.getId());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        //then frontend get the model, we can do some nice things

        //return the fragment, blogList, of the page blogs
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blogs/input")
    public String input(Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");

        model.addAttribute("user", user);
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());


        model.addAttribute("blog", new Blog());
        return "admin/blogs-input";
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, HttpSession session, Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());

        Blog blog = blogService.getBlog(id);

        System.out.println(blog.getTags());
        blog.initTagIds();

        User user = (User) session.getAttribute("user");

        model.addAttribute("user", user);

        model.addAttribute("blog", blog);
        return "admin/blogs-input";
    }

    //create new and save
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
            attributes.addFlashAttribute("message", "operation failed!");
        } else {
            attributes.addFlashAttribute("message", "operation success!");
        }

        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "operation success");
        return "redirect:/admin/blogs";
    }


}
