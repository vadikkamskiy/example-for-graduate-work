package ru.skypro.homework.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.entity.AdImageEntity;
import ru.skypro.homework.dto.response.AdsResponse;


@Component
public class AdMapper {

    private static final Logger log = LoggerFactory.getLogger(AdMapper.class);
    private static final byte[] EMPTY_IMAGE = new byte[0];

    public AdsResponse toResponse(AdEntity ad) {
        Objects.requireNonNull(ad, "ad must not be null");

        byte[] imageBytes = EMPTY_IMAGE;
        if (ad.getImage() != null && ad.getImage().getUrl() != null && !ad.getImage().getUrl().isBlank()) {
            try {
                imageBytes = loadImageFromUrl(ad.getImage().getUrl());
            } catch (UncheckedIOException ex) {
                log.warn("Не удалось загрузить изображение для объявления pk={} url={}: {}",
                        ad.getPk(), ad.getImage().getUrl(), ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                imageBytes = EMPTY_IMAGE;
            }
        }

        return new AdsResponse(
                ad.getAuthor(),
                imageBytes,
                ad.getPk(),
                ad.getPrice(),
                ad.getTitle()
        );
    }

    public Ad toDto(AdEntity entity) {
        Objects.requireNonNull(entity, "entity must not be null");

        byte[] file = EMPTY_IMAGE;
        if (entity.getImage() != null && entity.getImage().getUrl() != null && !entity.getImage().getUrl().isBlank()) {
            try {
                file = loadImageFromUrl(entity.getImage().getUrl());
            } catch (UncheckedIOException ex) {
                log.warn("Не удалось загрузить изображение для AdEntity pk={} url={}: {}",
                        entity.getPk(), entity.getImage().getUrl(), ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                file = EMPTY_IMAGE;
            }
        }

        return new Ad(
                entity.getAuthor(),
                entity.getImage(),
                file,
                entity.getPk(),
                entity.getPrice(),
                entity.getTitle(),
                entity.getDescription()
        );
    }

    public static Ad toDto(AdEntity e, AdImageEntity image) {
        Objects.requireNonNull(e, "AdEntity must not be null");

        byte[] file = EMPTY_IMAGE;
        if (image != null && image.getUrl() != null && !image.getUrl().isBlank()) {
            try {
                file = loadImageFromUrl(image.getUrl());
            } catch (UncheckedIOException ex) {
                LoggerFactory.getLogger(AdMapper.class).warn(
                        "Не удалось загрузить изображение (static) для объявления pk={} url={}: {}",
                        e.getPk(), image.getUrl(), ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage()
                );
                file = EMPTY_IMAGE;
            }
        }

        Ad dto = new Ad();
        dto.setPk(e.getPk());
        dto.setTitle(e.getTitle());
        dto.setPrice(e.getPrice());
        dto.setDescription(e.getDescription());
        dto.setAuthor(e.getAuthor());
        dto.setImageEntity(image);
        dto.setImage(file);

        return dto;
    }

    public AdEntity toEntity(Ad dto, UserEntity user) {
        Objects.requireNonNull(dto, "dto must not be null");
        Objects.requireNonNull(user, "user must not be null");

        return AdEntity.builder()
                .author(user.getPk())
                .image(dto.getImageEntity())
                .price(dto.getPrice())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }

    private static byte[] loadImageFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return EMPTY_IMAGE;
        }

        try {
            if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                try (InputStream in = new URL(imageUrl).openStream()) {
                    return in.readAllBytes();
                }
            } else {
                return Files.readAllBytes(Paths.get(imageUrl));
            }
        } catch (IOException ex) {
            throw new UncheckedIOException("Не удалось загрузить изображение: " + imageUrl, ex);
        } catch (RuntimeException ex) {
            throw new RuntimeException("Ошибка при загрузке изображения: " + imageUrl, ex);
        }
    }
}
