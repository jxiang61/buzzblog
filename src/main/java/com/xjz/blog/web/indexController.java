package com.xjz.blog.web;

import com.xjz.blog.NotFoundException;
import com.xjz.blog.pojo.User;
import com.xjz.blog.service.BlogService;
import com.xjz.blog.service.TagService;
import com.xjz.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class indexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size=6, sort={"updateTime"}, direction= Sort.Direction.DESC) Pageable pageable,
                        HttpSession session,
                        Model model) {

        model.addAttribute("page", blogService.listBlog(pageable));

        model.addAttribute("types", typeService.listTypeTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));

        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", null);
        }
        return "index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size=6, sort={"updateTime"}, direction= Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query,
                         Model model) {
        //"page" is the search result
        model.addAttribute("page", blogService.listBlog('%'+query+'%', pageable));
        model.addAttribute("query", query);

        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model, HttpSession session) {

        model.addAttribute("blog", blogService.getAndConvert(id));

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "blog";
    }


}
