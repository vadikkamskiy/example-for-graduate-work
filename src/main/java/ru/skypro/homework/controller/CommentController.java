package ru.skypro.homework.controller;

import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.Comment;
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


@RestController
@RequestMapping("/ads")
public class CommentController {
    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable int id) {
        return ResponseEntity.ok(new Comments(0, List.of()));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable int id, @RequestBody Comment comment) {
        return ResponseEntity.ok(comment);
    }
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable int id, @PathVariable int commentId) {
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable int id, @PathVariable int commentId, @RequestBody Comment comment) {
        return ResponseEntity.ok(comment);
    }
}
