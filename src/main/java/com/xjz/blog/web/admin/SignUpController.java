package com.xjz.blog.web.admin;


import com.xjz.blog.pojo.User;
import com.xjz.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/sign")
public class SignUpController {

    @Autowired
    UserService userService;

    @GetMapping("/sign-up")
    public String signUp() {
        return "admin/sign-up";
    }

    @PostMapping("/register")
    public String register(@Valid User user,
                           BindingResult result,
                           HttpSession session,
                           RedirectAttributes attributes,
                           Model model
                           ) {

        User u0 = userService.findUser(user.getUsername());

        if (u0 != null) {
            result.rejectValue("username", "nameError", "This username already exists!");
        }

        if (result.hasErrors()) {
            attributes.addFlashAttribute("message", "This username already exists!");
            return "redirect:/sign/sign-up";
        } else {
            User u = userService.saveUser(user);
            if (u == null) {
                attributes.addFlashAttribute("message", "register failed!");
                return "redirect:/sign/sign-up";
            } else {
                user.setPassword(null);
                //set current user to current session
                session.setAttribute("user", user);
                model.addAttribute("user", user);
                attributes.addFlashAttribute("message", "register success!");
            }
            return "admin/index";
        }
    }
}
