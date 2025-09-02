package com.board.board.service;


import com.board.board.dto.PostDto;
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
    public Page<PostDto> getPosts(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public PostDto getPost(Long id) {
        Post post = getPostEntity(id);
        return toDto(post);
    }

    public PostDto create(PostDto dto) {
        Post post = toEntity(dto);
        Post saved = postRepository.save(post);
        return toDto(saved);
    }

    public PostDto update(Long id, PostDto dto) {
        Post post = getPostEntity(id);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAuthor(dto.getAuthor());
        return toDto(post);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public void increaseViewCount(Long id) {
        Post post = getPostEntity(id);
        post.setViewCount(post.getViewCount() + 1);
    }

    private Post getPostEntity(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + id));
    }

    private PostDto toDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getViewCount()
        );
    }

    private Post toEntity(PostDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAuthor(dto.getAuthor());
        return post;
    }

}
