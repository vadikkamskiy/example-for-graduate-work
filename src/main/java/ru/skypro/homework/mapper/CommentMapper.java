package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.entity.CommentEntity;

public class CommentMapper {
    public static Comment toDto(CommentEntity entity, String authorFirstName) {
        if (entity == null) {
            return null;
        }
        Comment dto = new Comment();
        dto.setPk(entity.getPk());
        dto.setAuthorImage("image");
        dto.setText(entity.getText());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setAuthorFirstName(authorFirstName);
        return dto;
    }

    public static CommentEntity toEntity(Comment dto, Long adId, Long authorId) {
        if (dto == null) {
            return null;
        }
        CommentEntity entity = new CommentEntity();
        entity.setAuthor(dto.getAuthorId());
        entity.setText(dto.getText());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setAd(adId);;
        return entity;
    }
}
