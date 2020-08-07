package com.xjz.blog.web;

import com.xjz.blog.pojo.User;
import com.xjz.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ArchiveController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archives")
    public String archives(Model model, HttpSession session) {

        model.addAttribute("archiveMap", blogService.archiveBlog());
        model.addAttribute("blogCount", blogService.countBlog());

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        return "archives";
    }
}
