package com.xjz.blog.web.admin;

import com.xjz.blog.pojo.User;
import com.xjz.blog.service.BlogService;
import com.xjz.blog.service.BlogServiceImpl;
import com.xjz.blog.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private BlogServiceImpl blogService;

    @GetMapping
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/index")
    public String indexPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        return "admin/index";
    }

    @PostMapping("/login")
    public String login(@PageableDefault(size=6, sort={"updateTime"}, direction= Sort.Direction.DESC) Pageable pageable,
                        @RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes,
                        Model model) {
        User user = userService.checkUser(username, password);
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
            model.addAttribute("user", user);

            model.addAttribute("page", blogService.listBlog(pageable));
            return "redirect:/";
        } else {
            attributes.addFlashAttribute("message", "wrong username or password");
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

}
