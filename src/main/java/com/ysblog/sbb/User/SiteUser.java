package com.ysblog.sbb.User;

import com.ysblog.sbb.Comment.Comment;
import com.ysblog.sbb.Post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String nickname;

    private String password;

    @Column(unique = true)
    private String email;

    private LocalDate birthDate;

    private LocalDateTime joinDate;

    private String address;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Post> postList;

    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    private List<Comment> commentList;

    private String imageAddress;
}
