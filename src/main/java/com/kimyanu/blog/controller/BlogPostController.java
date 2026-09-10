package com.kimyanu.blog.controller;

import com.kimyanu.blog.model.BlogPost;
import com.kimyanu.blog.repository.BlogPostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BlogPostController {

    private final BlogPostRepository blogPostRepository;

    public BlogPostController(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @GetMapping({"/", "/posts"})
    public String list(Model model) {
        model.addAttribute("pinnedPosts", blogPostRepository.findPinned());
        model.addAttribute("posts", blogPostRepository.findNotPinned());
        return "posts/index";
    }

    @GetMapping("/posts/new")
    public String newPostForm(Model model) {
        model.addAttribute("post", new BlogPost());
        return "posts/write";
    }

    @PostMapping("/posts")
    public String createPost(@ModelAttribute BlogPost post) {
        blogPostRepository.createdPost(post);
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        try {
            BlogPost post = blogPostRepository.getBlogPostById(id);
            model.addAttribute("post", post);
            return "posts/post";
        } catch (IllegalArgumentException e) {
            return "redirect:/posts";
        }

    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        blogPostRepository.deletePostById(id);
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        BlogPost existingPost = blogPostRepository.getBlogPostById(id);
        model.addAttribute("post", existingPost);
        return "posts/write";
    }

    @PostMapping("/posts/{id}/edit")
    public String updatePost(@PathVariable Long id, @ModelAttribute BlogPost post) {
        blogPostRepository.updatePost(id, post);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/{id}/pin")
    public String updatePin(@PathVariable Long id) {
        BlogPost post = blogPostRepository.getBlogPostById(id);
        if (post == null) {
            throw new IllegalArgumentException("글을 찾을 수 없습니다. id=" + id);
        }
        blogPostRepository.updatePinnedPostStatus(id);
        return "redirect:/posts/" + id;
    }

}
