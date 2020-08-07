package com.xjz.blog.web;

import com.xjz.blog.pojo.Blog;
import com.xjz.blog.pojo.Comment;
import com.xjz.blog.pojo.User;
import com.xjz.blog.service.BlogService;
import com.xjz.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;


    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model, HttpSession session) {

        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));

        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        Long blogId = comment.getBlog().getId();
        Blog blog = blogService.getBlog(blogId);
        comment.setBlog(blog);

        ////////////////////////////////////////////////////
        User user = (User)session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            if (user.getType() == 1) {
                comment.setAdminComment(true);
            }
            comment.setNickname(user.getNickname());
        } else {
            //-------------every user has his own avatar, but not the only one user---------------------
            comment.setAvatar(avatar);
        }
        /////////////////////////////////////////////////////

        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }


}
