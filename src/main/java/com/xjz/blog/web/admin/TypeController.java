package com.xjz.blog.web.admin;

import com.xjz.blog.pojo.Type;
import com.xjz.blog.pojo.User;
import com.xjz.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    //here, pageable comes from front end
    //then we take it and process it on back end to retrieve the type list
    @GetMapping("/types")
    public String types(@PageableDefault(size=6, sort = {"id"}, direction = Sort.Direction.DESC)
                                    Pageable pageable,
                                    HttpSession session,
                                    Model model) {

        Page<Type> typePage = typeService.listType(pageable);

        model.addAttribute("page", typePage);

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model, HttpSession session) {
        model.addAttribute("type", new Type());

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        return "admin/types-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, HttpSession session, Model model) {
        model.addAttribute("type", typeService.getType(id));

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        return "admin/types-input";
    }

    @PostMapping("/types")
    public String post(@Valid Type type,
                       BindingResult result,
                       RedirectAttributes attributes,
                       HttpSession session,
                       Model model) {
        //duplication validation
        Type t0 = typeService.getTypeByName(type.getName());
        if (t0 != null) {
            // s: must be identical to "name" in Type class
            result.rejectValue("name", "nameError", "This name already exists!");
        }

        //result comes from the validation
        if (result.hasErrors()) {

            User user = (User) session.getAttribute("user");
            model.addAttribute("user", user);

            return "admin/types-input";
        }

        Type t = typeService.saveType(type);

        if (t == null) {
            attributes.addFlashAttribute("message", "operation failed!");
        } else {
            attributes.addFlashAttribute("message", "operation success!");
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String post(@Valid Type type,
                       BindingResult result,
                       @PathVariable Long id,
                       RedirectAttributes attributes,
                       HttpSession session,
                       Model model) {
        //duplication validation
        Type t0 = typeService.getTypeByName(type.getName());
        if (t0 != null) {
            // s: must be identical to "name" in Type class
            result.rejectValue("name", "nameError", "This name already exists!");
        }

        //result comes from the validation
        if (result.hasErrors()) {

            User user = (User) session.getAttribute("user");
            model.addAttribute("user", user);

            return "admin/types-input";
        }

        Type t = typeService.updateType(id, type);

        if (t == null) {
            attributes.addFlashAttribute("message", "operation failed!");
        } else {
            attributes.addFlashAttribute("message", "operation success!");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,  RedirectAttributes attributes) {
        typeService.deleteType(id);
        //show message
        attributes.addFlashAttribute("message", "operation success!");
        return "redirect:/admin/types";
    }

}
