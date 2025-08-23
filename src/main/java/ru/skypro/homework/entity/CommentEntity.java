package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column(name = "author", nullable = false)
    private Long author;

    @Column(name = "author_avatar")
    private String authorAvatar;

    @Column(name = "ad", nullable = false)
    private Long ad;

    @Column(name = "created_at", nullable = false)
    private String createdAt;

    @Column(name = "text", nullable = false)
    private String text;

}
