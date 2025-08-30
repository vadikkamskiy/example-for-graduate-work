package ru.skypro.homework.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.CommentEntity;


public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByAd(Long ad);
    CommentEntity findByPk(Long pk);
}