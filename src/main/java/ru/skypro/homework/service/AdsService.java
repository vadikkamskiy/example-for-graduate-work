package ru.skypro.homework.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.request.CreateOrUpdateAd;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.mapper.AdMapper;


@Service
public class AdsService {
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

    public AdsService(AdsRepository adsRepository,
                        UserRepository userRepository,
                        AdMapper adMapper) {
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
        this.adMapper = adMapper;
    }

    public Ads getAllAds() {
        List<Ad> ads = adsRepository.findAll().stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());
        return new Ads(ads.size(), ads);
    }

    public Ad createAd(String username, CreateOrUpdateAd ad, String image) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AdEntity adEntity = new AdEntity();
        adEntity.setAuthor(userEntity.getPk());
        adEntity.setTitle(ad.getTitle());
        adEntity.setDescription(ad.getDescription());
        adEntity.setPrice(ad.getPrice());
        adEntity.setImage(image);
        AdEntity savedAd = adsRepository.save(adEntity);
        return adMapper.toDto(savedAd);
    }

    public Ad getAdById(Long id) {
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));
        return adMapper.toDto(adEntity);
    }

    public void deleteAdById(String username, Long id) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));
        if (!adEntity.getAuthor().equals(userEntity.getPk())) {
            throw new RuntimeException("User is not the author of the ad");
        }
        adsRepository.delete(adEntity);
    }

    public Ad updateAd(String username, Long id, CreateOrUpdateAd ad) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));
        if (!adEntity.getAuthor().equals(userEntity.getPk())) {
            throw new RuntimeException("User is not the author of the ad");
        }
        adEntity.setTitle(ad.getTitle());
        adEntity.setDescription(ad.getDescription());
        adEntity.setPrice(ad.getPrice());
        AdEntity updatedAd = adsRepository.save(adEntity);
        return adMapper.toDto(updatedAd);
    }

    public Ads getAdsByUser(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Ad> ads = adsRepository.findByAuthor(userEntity.getPk()).stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());
        return new Ads(ads.size(), ads);
    }

    public Ad updateAdImage(String username, Long id, String image) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));
        if (!adEntity.getAuthor().equals(userEntity.getPk())) {
            throw new RuntimeException("User is not the author of the ad");
        }
        adEntity.setImage(image);
        AdEntity updatedAd = adsRepository.save(adEntity);
        return adMapper.toDto(updatedAd);
    }
}
