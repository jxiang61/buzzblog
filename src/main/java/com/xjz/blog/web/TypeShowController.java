package com.xjz.blog.web;

import com.xjz.blog.pojo.Type;
import com.xjz.blog.pojo.User;
import com.xjz.blog.searchobject.BlogQuery;
import com.xjz.blog.service.BlogService;
import com.xjz.blog.service.TypeService;
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
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size=8, sort={"updateTime"}, direction= Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id,
                        HttpSession session,
                        Model model) {

        List<Type> types = typeService.listTypeTop(10000);
        if (id == -1) {
            id = types.get(0).getId();
        }

        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types", types);
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("activeTypeId", id);

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        return "types";
    }
}
