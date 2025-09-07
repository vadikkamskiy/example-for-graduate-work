package ru.skypro.homework.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AdEntity;

public interface AdsRepository extends JpaRepository<AdEntity, Long> {
    public List<AdEntity> findByAuthor(Long author);
    Optional<AdEntity> findByPk(Long pk);
}
