package com.board.board.service;


import com.board.board.entity.Post;
import com.board.board.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class PostService {
    
    private final PostRepository postRepository;

    public PostService(PostRepository postrepository) {
        this.postRepository = postrepository;
    }
    
    @Transactional(readOnly = true)
    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + id));
    }

    public Post create(Post post) {
        return postRepository.save(post);
    }

    public Post update(Long id, String title, String content, String author) {
        Post post = getPost(id);
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        return post;
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public void increaseViewCount(Long id) {
        Post post = getPost(id);
        post.setViewCount(post.getViewCount() + 1
        );
    }

}
