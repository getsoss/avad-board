package com.board.board.controller;

import com.board.board.dto.PostDto;
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
        Page<PostDto> posts = postService.getPosts(PageRequest.of(page, size));
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    @GetMapping("/new")
    public String newFrom(Model model) {
        model.addAttribute("post", new PostDto());
        return "posts/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("post") PostDto post, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "posts/form";
        }
        postService.create(post);
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        postService.increaseViewCount(id);
        PostDto post = postService.getPost(id);
        model.addAttribute("post", post);
        return "posts/view";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        PostDto post = postService.getPost(id);
        model.addAttribute("post", post);
        return "posts/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("post") PostDto post, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "posts/form";
        }
        postService.update(id, post);
        return "redirect:/posts/{id}";
    }
    
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/posts";
    }
}
