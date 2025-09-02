package ru.skypro.homework.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AdImageEntity;

public interface AdsImageRepository extends JpaRepository<AdImageEntity, Long> {
    Optional<AdImageEntity> findByAd_Pk(Long adId);
}
