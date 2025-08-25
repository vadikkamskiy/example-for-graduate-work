package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

@Component
public class AdMapper {

    public Ad toDto(AdEntity entity) {
        return new Ad(
                entity.getAuthor(),
                "image url",
                entity.getPk(),
                entity.getPrice(),
                entity.getTitle(),
                entity.getDescription()
                
        );
    }

    public AdEntity toEntity(Ad dto, UserEntity user) {
        return AdEntity.builder()
                .author(user.getPk())
                .image("image url")
                .price(dto.getPrice())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }
}

