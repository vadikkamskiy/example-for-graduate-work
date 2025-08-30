package ru.skypro.homework.service;

import java.time.LocalDateTime;
import java.util.List;

import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.request.CommentRequest;

import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                             UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public Comments getCommentsForAd(Long adId) {
        List<CommentEntity> entities = commentRepository.findByAd(adId);
        List<Comment> comments = entities.stream()
                .map(entity -> {
                    UserEntity user = userRepository.findById(entity.getAuthor()).orElse(null);
                    String authorFirstName = (user != null) ? user.getFirstName() : "Unknown";
                    return CommentMapper.toDto(entity, authorFirstName);
                })
                .toList();
        return new Comments(comments.size(), comments);
    }

    public Comment addComment(String username, Long adId, String comment) {
        UserEntity user = userRepository.findByUsername(username).get();
        if (user == null) {
            return null;
        }
        CommentEntity entity = new CommentEntity();
        entity.setAd(adId);
        entity.setAuthor(user.getPk());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setText(comment);
        CommentEntity savedEntity = commentRepository.save(entity);
        return CommentMapper.toDto(savedEntity, user.getFirstName());
    }

    public void deleteComment(String username ,Long adId, Long commentId) {
        UserEntity user = userRepository.findByUsername(username).get();
        if (user == null) {
            return;
        }
        CommentEntity entity = commentRepository.findByPk(commentId);
        if (entity != null && entity.getAd().equals(adId) && entity.getAuthor().equals(user.getPk())) {
            commentRepository.delete(entity);
        }
    }

    public Comment updateComment(String username, int commentId, CommentRequest comment) {
        UserEntity user = userRepository.findByUsername(username).get();
        if (user == null) {
            return null;
        }
        CommentEntity entity = commentRepository.findByPk((long) commentId);
        if (entity != null && entity.getAuthor().equals(user.getPk())) {
            entity.setText(comment.getText());
            CommentEntity updatedEntity = commentRepository.save(entity);
            return CommentMapper.toDto(updatedEntity, user.getFirstName());
        }
        return null;

    }
}
