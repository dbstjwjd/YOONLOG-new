package com.ysblog.sbb.Post;

import com.ysblog.sbb.Comment.Comment;
import com.ysblog.sbb.Comment.CommentService;
import com.ysblog.sbb.CommonUtil;
import com.ysblog.sbb.User.SiteUser;
import com.ysblog.sbb.User.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    private final UserService userService;

    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(PostForm postForm, Model model, Principal principal) {
        SiteUser user = this.userService.getUser(principal.getName());
        model.addAttribute("user", user);
        return "post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid PostForm postForm, BindingResult bindingResult, Principal principal, Model model) {
        SiteUser user = this.userService.getUser(principal.getName());
        model.addAttribute("user", user);
        if (bindingResult.hasErrors()) {
            return "post_form";
        }
        this.postService.createPost(postForm.getSubject(), postForm.getContent(), postForm.getCategory(), user);
        return "redirect:/";
    }

    @GetMapping("/main")
    public String list(Model model, Principal principal, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw,
                       @RequestParam(value = "category", defaultValue = "") String category) {
        Page<Post> paging = this.postService.getList(page, kw, category);
        if (principal != null) {
            SiteUser user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);
        }
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("category", category);
        return "main";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, Principal principal,
                         @RequestParam(value = "page", defaultValue = "0") int page) {
        Post post = this.postService.getPost(id);
        Page<Comment> paging = this.commentService.getList(post, page);
        if (principal != null) {
            SiteUser user = this.userService.getUser(principal.getName());
            model.addAttribute("user", user);

        }
        model.addAttribute("paging", paging);
        model.addAttribute("post", post);
        return "post_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(PostForm postForm, @PathVariable("id") Integer id, Principal principal, Model model) {
        Post post = this.postService.getPost(id);
        SiteUser user = this.userService.getUser(principal.getName());
        model.addAttribute("user", user);
        if (!post.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        postForm.setCategory(post.getCategory());
        postForm.setSubject(post.getSubject());
        postForm.setContent(post.getContent());
        return "post_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Integer id, Principal principal,
                         @Valid PostForm postForm, BindingResult bindingResult) {
        Post post = this.postService.getPost(id);
        if (!post.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        if (bindingResult.hasErrors())
            return "post_form";
        this.postService.modifyPost(post, postForm.getCategory(), postForm.getSubject(), postForm.getContent());
        return String.format("redirect:/post/detail/%s", id);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Principal principal) {
        Post post = this.postService.getPost(id);
        if (!post.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        this.postService.deletePost(post);
        return "redirect:/";
    }

    @GetMapping("/like/{id}")
    @PreAuthorize("isAuthenticated()")
    public String vote(@PathVariable("id") Integer id, Principal principal) {
        SiteUser user = this.userService.getUser(principal.getName());
        Post post = this.postService.getPost(id);
        this.postService.likePost(post, user);
        return String.format("redirect:/post/detail/%s", id);
    }
}
