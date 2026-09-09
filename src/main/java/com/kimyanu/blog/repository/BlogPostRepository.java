package com.kimyanu.blog.repository;

import com.kimyanu.blog.model.BlogPost;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BlogPostRepository {
    private final List<BlogPost> blogPosts = new ArrayList<>();
    private AtomicLong idGenerator = new AtomicLong(0);

    public BlogPostRepository() {
        blogPosts.add(new BlogPost(idGenerator.incrementAndGet(), "스프링 부트로 만든 첫 CRUD", "글을 저장하고 불러오는 가장 단순한 형태부터 만들어 봤습니다.", LocalDateTime.now()));
        blogPosts.add(new BlogPost(idGenerator.incrementAndGet(), "성수동 골목의 작은 책방", "종이 냄새와 조용한 사람들 사이에서 두 시간을 보냈습니다.", LocalDateTime.now()));
        blogPosts.add(new BlogPost(idGenerator.incrementAndGet(), "덜어내는 연습", "화면에서 무언가를 지울 때마다 남은 것이 더 잘 보입니다.", LocalDateTime.now()));
    }

    public List<BlogPost> findAll() {
        return blogPosts;
    }

    public void createdPost(BlogPost post) {
        post.setId((long) (blogPosts.size() + 1));
        post.setCreatedAt(LocalDateTime.now());
        blogPosts.add(post);
    }

    public BlogPost getBlogPostById(Long id) {
        BlogPost existedPost = blogPosts.stream()
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (existedPost == null) {
            throw new IllegalArgumentException("글을 찾을 수 없습니다. id=" + id);
        } else {
            return existedPost;
        }
    }

    public void deletePostById(Long id) {
//        BlogPost existedPost = getBlogPostById(id);
//        blogPosts.remove(existedPost);
        blogPosts.removeIf(post -> post.getId().equals(id));
    }

    public void updatePost(Long id, BlogPost post) {
        BlogPost existedPost = getBlogPostById(id);
        existedPost.setTitle(post.getTitle());
        existedPost.setContent(post.getContent());
    }
}
