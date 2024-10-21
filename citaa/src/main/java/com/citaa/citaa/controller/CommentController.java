package com.citaa.citaa.controller;

import com.citaa.citaa.model.Comment;
import com.citaa.citaa.model.User;
import com.citaa.citaa.service.CommentService;
import com.citaa.citaa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;


    @PostMapping("/project/{projectId}")
    public Comment createComment(@RequestBody Comment comment,
                                 @RequestHeader("Authorization")String jwt,
                                 @PathVariable("projectId") int projectId) throws Exception {
        User user = userService.findByJwt(jwt);
        Comment createComment = commentService.createComment(comment,projectId,jwt);
        return createComment;
    }

    @PutMapping("/like/{commentId}")
    public Comment likedComment(@RequestHeader("Authorization")String jwt,
                                @PathVariable("commentId") int commentId) throws Exception {
        User user = userService.findByJwt(jwt);
        Comment likedComment = commentService.likeComment(commentId,jwt);
        return likedComment;
    }
}
