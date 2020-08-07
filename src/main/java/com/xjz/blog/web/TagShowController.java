package com.xjz.blog.web;

import com.xjz.blog.pojo.Tag;
import com.xjz.blog.pojo.User;
import com.xjz.blog.service.BlogService;
import com.xjz.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size=8, sort={"updateTime"}, direction= Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id,
                        HttpSession session,
                        Model model) {

        List<Tag> tags = tagService.listTagTop(10000);
        if (id == -1) {
            id = tags.get(0).getId();
        }

        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.listBlog(id, pageable));
        model.addAttribute("activeTagId", id);

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "tags";
    }
}
