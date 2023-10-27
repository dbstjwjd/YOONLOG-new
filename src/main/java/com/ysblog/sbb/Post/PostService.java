package com.ysblog.sbb.Post;

import com.ysblog.sbb.DataNotFoundException;
import com.ysblog.sbb.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void createPost(String subject, String content, String category, SiteUser author) {
        Post post = new Post();
        post.setSubject(subject);
        post.setContent(content);
        post.setCategory(category);
        post.setAuthor(author);
        post.setCreateDate(LocalDateTime.now());
        this.postRepository.save(post);
    }

    public Page<Post> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.postRepository.findAll(pageable);
    }

    public Post getPost(Integer id) {
        Optional<Post> op = this.postRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        } else {
            throw new DataNotFoundException("post not found");
        }
    }

    public void modifyPost(Post post, String category, String subject, String content) {
        post.setCategory(category);
        post.setSubject(subject);
        post.setContent(content);
        post.setModifyDate(LocalDateTime.now());
        this.postRepository.save(post);
    }

    public void deletePost(Post post) {
        this.postRepository.delete(post);
    }

}
