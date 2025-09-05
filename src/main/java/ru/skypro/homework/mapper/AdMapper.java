package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.entity.AdImageEntity;

@Component
public class AdMapper {

    public Ad toDto(AdEntity entity) {
        return new Ad(
                entity.getAuthor(),
                entity.getImage(),
                null,
                entity.getPk(),
                entity.getPrice(),
                entity.getTitle(),
                entity.getDescription()
                
        );
    }
    public static Ad toDto(AdEntity e, AdImageEntity image) {
        Ad dto = new Ad();
        dto.setPk(e.getPk());
        dto.setTitle(e.getTitle());
        dto.setPrice(e.getPrice());
        dto.setDescription(e.getDescription());
        dto.setAuthor(e.getAuthor());
        dto.setImageEntity(image);
        dto.setImage(image.getUrl());

        return dto;
    }

    public AdEntity toEntity(Ad dto, UserEntity user) {
        return AdEntity.builder()
                .author(user.getPk())
                .image(dto.getImageEntity())
                .price(dto.getPrice())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }
}

