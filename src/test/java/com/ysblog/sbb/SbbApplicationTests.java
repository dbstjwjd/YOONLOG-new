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
		Post post = this.postService.getPost(18);
		SiteUser user = this.userService.getUser("bbb");
		for (int i = 1; i <= 100; i++) {
			this.commentService.createComment(String.format("답변 페이징 테스트 %d", i), post, user);
		}
	}

}
