package ru.skypro.homework.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import ru.skypro.homework.entity.AdImageEntity;

@Entity
@Table(name = "ads")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column(name = "author", nullable = false)
    private Long author;

    @OneToOne(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private AdImageEntity image;

    @Column(name = "price")
    private Integer price;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

}

