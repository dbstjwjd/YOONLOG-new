package com.ysblog.sbb;

import com.ysblog.sbb.Comment.Comment;
import com.ysblog.sbb.Comment.CommentService;
import com.ysblog.sbb.Post.Post;
import com.ysblog.sbb.Post.PostService;
import com.ysblog.sbb.User.SiteUser;
import com.ysblog.sbb.User.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;
import java.time.LocalDateTime;

@SpringBootTest
class SbbApplicationTests {
	@Autowired
	private CommentService commentService;

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;

	@Test
	void contextLoads() {
		SiteUser user = this.userService.getUser("aaa");

		Post post = new Post();
		for (int i = 1; i <= 30; i++) {
			this.postService.createPost(String.format("테스트용 게시물 %d", i), "내용없음", "개발 일지", user);
		}
	}

}
