package ru.skypro.homework.controller;

import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.request.CommentRequest;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.service.CommentService;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "Comments", description = "Controller for managing comments on advertisements")
@RestController
@RequestMapping("/ads")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Get comments for an ad", description = "Retrieves all comments for a specific advertisement")
    @GetMapping("/{id}/comments")
    public Comments getComments(@PathVariable Long id) {
        return commentService.getCommentsForAd(id);
    }

    @Operation(summary = "Add a comment to an ad", description = "Creates a new comment for a specific advertisement")
    @PostMapping("/{id}/comments")
    public ResponseEntity<String> addComment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody CommentRequest request) {
        return commentService.addComment(userDetails.getUsername(), id, request.getText()) != null ?
            ResponseEntity.status(HttpStatus.CREATED).body("Comment added successfully") :
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add comment");
    }

    @Operation(summary = "Delete a comment by ID", description = "Delete comment by its ID")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/comments/{commentId}")
    public void deleteComment(@AuthenticationPrincipal UserDetails userDetails , @PathVariable Long id, @PathVariable Long commentId) {
        commentService.deleteComment(userDetails.getUsername(), id, commentId);
    }
    
    @Operation(summary = "Update a comment by ID", description = "Updates a specific comment for an advertisement")
    @PatchMapping("/{id}/comments/{commentId}")
    public Comment updateComment(@AuthenticationPrincipal UserDetails userDetails,@PathVariable int id, @PathVariable int commentId, @RequestBody CommentRequest comment) {
        return commentService.updateComment(userDetails.getUsername(),commentId, comment);
    }
}
