package com.citaa.citaa.service;

import com.citaa.citaa.model.Comment;
import com.citaa.citaa.model.Project;
import com.citaa.citaa.model.User;
import com.citaa.citaa.repository.CommentRepository;
import com.citaa.citaa.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProjectService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectRepository postReository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepository projectRepository;


    public Comment createComment(Comment comment, int projectId, String jwt) throws Exception {

        User user = userService.findByJwt(jwt);
        Project project = projectService.findProjectById(projectId);

        comment.setUser(user);
        comment.setContent(comment.getContent());
        comment.setCreateAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        project.getComments().add(savedComment);
        projectRepository.save(project);
        return savedComment;
    }


    public Comment likeComment(Integer commentId, String jwt) throws Exception {
        Comment comment = findCommentById(commentId);
        User user = userService.findByJwt(jwt);

        if(!comment.getLiked().contains(user)){
            comment.getLiked().add(user);
        }else{
            comment.getLiked().remove(user);
        }
        return commentRepository.save(comment);
    }

    public Comment findCommentById(Integer commentId) throws Exception {
        Optional<Comment> opt = commentRepository.findById(commentId);
        if(opt.isEmpty()){
            throw new Exception("Comment not exist");
        }
        return opt.get();
    }
}
