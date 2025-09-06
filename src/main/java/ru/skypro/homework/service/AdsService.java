package ru.skypro.homework.service;

import java.util.List;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.EntityNotFoundException;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.request.CreateOrUpdateAd;
import ru.skypro.homework.dto.response.AdsResponse;
import ru.skypro.homework.dto.response.MyAdsResponse;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.entity.AdImageEntity;
import ru.skypro.homework.repository.AdsImageRepository;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.mapper.AdMapper;


@Service
public class AdsService {
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final AdsImageRepository adImageRepository;
    private final AdMapper adMapper;

    public AdsService(AdsRepository adsRepository,
                        UserRepository userRepository,
                        AdMapper adMapper,
                        AdsImageRepository adImageRepository) {
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
        this.adMapper = adMapper;
        this.adImageRepository = adImageRepository;
    }

    public Ads getAllAds() {
        List<Ad> ads = adsRepository.findAll().stream()
                .map(adMapper::toDto)
                .collect(Collectors.toList());
        return new Ads(ads.size(), ads);
    }
    public Ad createAd(String username, CreateOrUpdateAd adDto, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must be provided");
        }   

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AdEntity ad = new AdEntity();
        ad.setTitle(adDto.getTitle());
        ad.setDescription(adDto.getDescription());
        ad.setPrice(adDto.getPrice());
        ad.setAuthor(user.getPk());
        AdEntity savedAd = adsRepository.save(ad);

        String storageKey = saveFileLocally(file);
        String url = "/src/images/" + storageKey;

        AdImageEntity image = new AdImageEntity();
        image.setAd(savedAd);
        image.setStorageKey(storageKey);
        image.setUrl(url);
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setPosition(0);
        image.setIsPrimary(true);
        ad.setImage(image);
        adImageRepository.save(image);
        savedAd.setImage(image);
        savedAd = adsRepository.save(savedAd);

        return adMapper.toDto(savedAd);
    }

    public AdsResponse getAdById(int id) {
        AdImageEntity image = adImageRepository.findByAd_Pk(Long.valueOf(id)).get();
        AdEntity adEntity = adsRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("Ad not found with id: " + id));
        adEntity.setImage(image);
        return adMapper.toResponse(adEntity);
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

    public Ad updateAdImage(String username, Long adId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must be provided");
        }

        AdEntity ad = adsRepository.findByPk(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        UserEntity author = userRepository.findById(ad.getAuthor())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        if (!username.equals(author.getUsername())) {
            throw new RuntimeException("User is not the author of the ad");
        }

        String storageKey = saveFileLocally(file); 
        String url = "/uploads/" + storageKey;

        AdImageEntity image = adImageRepository.findByAd_Pk(adId)
                .orElseGet(() -> new AdImageEntity());

        image.setAd(ad);
        image.setStorageKey(storageKey);
        image.setUrl(url);
        // image.setFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setPosition(0);
        image.setIsPrimary(true);

        adImageRepository.save(image);

        ad.setImage(image);

        return adMapper.toDto(ad);
    }

    public String saveFileLocally(MultipartFile file) throws IOException {
        String folder = "src\\images"; 
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(folder, filename);

        Files.createDirectories(path.getParent());

        Files.write(path, file.getBytes());

        return filename;
    }
}
