package ru.skypro.homework.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ru.skypro.homework.entity.AdEntity;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ad_images", indexes = {
        @Index(name = "idx_ad_images_ad", columnList = "ad_id"),
        @Index(name = "idx_ad_images_ad_pos", columnList = "ad_id, position")
})
public class AdImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ad_id", nullable = false, unique = true)    @JsonIgnore
    private AdEntity ad;

    @Column(name = "storage_key", length = 1024)
    private String storageKey;

    @Column(name = "url", length = 2048)
    private String url;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "size")
    private Long size;

    @Column(name = "position")
    private Integer position;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
