package com.xjz.blog.web.admin;

import com.xjz.blog.pojo.Tag;
import com.xjz.blog.pojo.User;
import com.xjz.blog.service.TagService;
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
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size=6, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       Model model,
                       HttpSession session) {

        Page<Tag> tagPage = tagService.listTag(pageable);

        model.addAttribute("page", tagPage);

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        return "admin/tags";
    }

    //create new
    @GetMapping("/tags/input")
    public String input(Model model, HttpSession session) {
        model.addAttribute("tag", new Tag());

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        return "admin/tags-input";
    }

    //edit existed tag
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model, HttpSession session) {
        model.addAttribute("tag", tagService.getTag(id));

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        return "admin/tags-input";
    }

    //post a new tag
    @PostMapping("/tags")
    public String post(@Valid Tag tag,
                       BindingResult result,
                       RedirectAttributes attributes,
                       Model model,
                       HttpSession session) {
        //duplication validation
        Tag t0 = tagService.getTagByName(tag.getName());
        if (t0 != null) {
            // s: must be identical to "name" in Tag class
            result.rejectValue("name", "nameError", "This name already exists!");
        }

        //result comes from the validation
        if (result.hasErrors()) {
            User user = (User) session.getAttribute("user");
            model.addAttribute("user", user);
            return "admin/tags-input";
        }

        Tag t = tagService.saveTag(tag);

        if (t == null) {
            attributes.addFlashAttribute("message", "operation failed!");
        } else {
            attributes.addFlashAttribute("message", "operation success!");
        }
        return "redirect:/admin/tags";
    }

    //post an edited tag
    @PostMapping("/tags/{id}")
    public String post(@Valid Tag tag,
                       BindingResult result,
                       @PathVariable Long id,
                       RedirectAttributes attributes,
                       Model model,
                       HttpSession session) {
        //duplication validation
        Tag t0 = tagService.getTagByName(tag.getName());
        if (t0 != null) {
            // s: must be identical to "name" in Type class
            result.rejectValue("name", "nameError", "This name already exists!");
        }

        //result comes from the validation
        if (result.hasErrors()) {
            User user = (User) session.getAttribute("user");
            model.addAttribute("user", user);
            return "admin/tags-input";
        }

        Tag t = tagService.updateTag(id, tag);

        if (t == null) {
            attributes.addFlashAttribute("message", "operation failed!");
        } else {
            attributes.addFlashAttribute("message", "operation success!");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id,
                         RedirectAttributes attributes) {
        tagService.deleteTag(id);
        //show message
        attributes.addFlashAttribute("message", "operation success!");
        return "redirect:/admin/tags";
    }

}
