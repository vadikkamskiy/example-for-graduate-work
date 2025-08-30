package ru.skypro.homework.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    void deleteByUsername(String username);
    Optional<UserEntity> findById(Long id);
}
