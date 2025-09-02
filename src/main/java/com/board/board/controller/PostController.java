package com.board.board.controller;

import com.board.board.entity.Post;
import com.board.board.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/posts")
public class PostController {
   
    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }
    
    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model) {
        Page<Post> posts = postService.getPosts(PageRequest.of(page, size));
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    @GetMapping("/new")
    public String newFrom(Model model) {
        model.addAttribute("post", new Post());
        return "posts/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("post") Post post, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "posts/form";
        }
        postService.create(post);
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        postService.increaseViewCount(id);
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        return "posts/view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Post post = postService.getPost(id);
        model.addAttribute("post", post);
        return "posts/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("post") Post post, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "posts/form";
        }
        postService.update(id, post.getTitle(), post.getContent(), post.getAuthor());
        return "redirect:/posts/{id}";
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/posts";
    }
}
