package ru.skypro.homework.mapper;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.entity.AdImageEntity;
import ru.skypro.homework.dto.response.AdsResponse;
import ru.skypro.homework.dto.response.CurrentAdResponse;

/**
 * AdMapper без загрузки http(s) изображений в память.
 * HTTP-изображения откладываются — клиенту нужно использовать URL напрямую.
 */
@Component
public class AdMapper {

    private static final Logger log = LoggerFactory.getLogger(AdMapper.class);
    private static final byte[] EMPTY_IMAGE = new byte[0];

    private final ResourceLoader resourceLoader;
    private final String imagesDir;

    public AdMapper(ResourceLoader resourceLoader,
                    @Value("${images.dir:}") String imagesDir) {
        this.resourceLoader = resourceLoader;
        this.imagesDir = imagesDir != null ? imagesDir.trim() : "";
    }

    public AdsResponse toResponse(AdEntity ad) {
        Objects.requireNonNull(ad, "ad must not be null");


        byte[] imageBytes = EMPTY_IMAGE;
        if (ad.getImage() != null && ad.getImage().getUrl() != null && !ad.getImage().getUrl().isBlank()) {
            String url = ad.getImage().getUrl();
            if (isHttpUrl(url)) {
                log.debug("HTTP изображение отложено для объявления pk={} url={}", ad.getPk(), url);
            } else {
                try {
                    imageBytes = loadLocalImage(url);
                } catch (UncheckedIOException ex) {
                    log.warn("Не удалось загрузить локальное изображение для объявления pk={} url={} : {}",
                            ad.getPk(), url, ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                    imageBytes = EMPTY_IMAGE;
                } catch (RuntimeException ex) {
                    log.warn("Ошибка при загрузке локального изображения для объявления pk={} url={} : {}",
                            ad.getPk(), url, ex.getMessage());
                    imageBytes = EMPTY_IMAGE;
                }
            }
        }
        String image = ad.getImage().getUrl();

        return new AdsResponse(
                ad.getAuthor(),
                loadLocalImage(image),
                ad.getPk(),
                ad.getPrice(),
                ad.getTitle()
        );
    }

    public CurrentAdResponse toCurrentResponse(AdEntity ad,UserEntity user) {
        Objects.requireNonNull(ad, "ad must not be null");

        byte[] imageBytes = EMPTY_IMAGE;
        if (ad.getImage() != null && ad.getImage().getUrl() != null && !ad.getImage().getUrl().isBlank()) {
            String url = ad.getImage().getUrl();
            if (isHttpUrl(url)) {
                log.debug("HTTP изображение отложено для объявления pk={} url={}", ad.getPk(), url);
            } else {
                try {
                    imageBytes = loadLocalImage(url);
                } catch (UncheckedIOException ex) {
                    log.warn("Не удалось загрузить локальное изображение для объявления pk={} url={} : {}",
                            ad.getPk(), url, ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
                    imageBytes = EMPTY_IMAGE;
                } catch (RuntimeException ex) {
                    log.warn("Ошибка при загрузке локального изображения для объявления pk={} url={} : {}",
                            ad.getPk(), url, ex.getMessage());
                    imageBytes = EMPTY_IMAGE;
                }
            }
        }

        return new CurrentAdResponse(
                ad.getPk(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                ad.getDescription(),
                loadLocalImage(ad.getImage().getUrl()),
                user.getPhone(),
                ad.getPrice(),
                ad.getTitle()
        );
    }

    public Ad toDto(AdEntity entity) {
        Objects.requireNonNull(entity, "entity must not be null");
        String image = entity.getImage().getUrl();

        return new Ad(
                entity.getAuthor(),
                entity.getImage(),
                loadLocalImage(image),
                entity.getPk(),
                entity.getPrice(),
                entity.getTitle(),
                entity.getDescription()
        );
    }

    public Ad toDto(AdEntity e, AdImageEntity image) {
        Objects.requireNonNull(e, "AdEntity must not be null");


        Ad dto = new Ad();
        dto.setPk(e.getPk());
        dto.setTitle(e.getTitle());
        dto.setPrice(e.getPrice());
        dto.setDescription(e.getDescription());
        dto.setAuthor(e.getAuthor());
        dto.setImageEntity(image);
        dto.setImage(loadLocalImage(image.getUrl()));

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
    private static boolean isHttpUrl(String url) {
        String lower = url.toLowerCase();
        return lower.startsWith("http://") || lower.startsWith("https://");
    }

        private byte[] loadLocalImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return EMPTY_IMAGE;
        }

        final String raw = imageUrl;
        final String normalized = raw.replace('\\', '/').trim();

        final String filename = Paths.get(normalized).getFileName().toString();

        final boolean looksAbsolute = Paths.get(normalized).isAbsolute() || normalized.matches("^[A-Za-z]:/.*");
        final String trimmedForRelative = looksAbsolute ? normalized : normalized.replaceFirst("^/+", "");

        List<String> tried = new ArrayList<>();

        try {
            if (imagesDir != null && !imagesDir.isBlank()) {
                Path candidate = Paths.get(imagesDir).resolve(filename);
                tried.add(candidate.toAbsolutePath().toString());
                log.debug("Пробуем images.dir: {}", candidate.toAbsolutePath());
                if (Files.exists(candidate)) {
                    return Files.readAllBytes(candidate);
                }
            }

            // B) file: URI
            if (normalized.startsWith("file:")) {
                try {
                    URI uri = new URI(normalized);
                    Path p = Paths.get(uri);
                    tried.add(p.toAbsolutePath().toString());
                    log.debug("Пробуем file URI: {}", p.toAbsolutePath());
                    if (Files.exists(p)) {
                        return Files.readAllBytes(p);
                    }
                } catch (URISyntaxException ex) {
                    log.debug("Некорректный file URI: {}", normalized);
                }
            }

            // C) прямой путь как дано
            Path direct = Paths.get(normalized);
            tried.add(direct.toAbsolutePath().toString());
            log.debug("Пробуем прямой путь: {}", direct.toAbsolutePath());
            if (Files.exists(direct)) {
                return Files.readAllBytes(direct);
            }

            // D) относительный к user.dir
            Path relative = Paths.get(System.getProperty("user.dir")).resolve(trimmedForRelative);
            tried.add(relative.toAbsolutePath().toString());
            log.debug("Пробуем относительный к user.dir: {}", relative.toAbsolutePath());
            if (Files.exists(relative)) {
                return Files.readAllBytes(relative);
            }

            // E) classpath
            String cpCandidate = trimmedForRelative; // без ведущих слэшей
            Resource resource = resourceLoader.getResource("classpath:" + cpCandidate);
            tried.add("classpath:" + cpCandidate);
            log.debug("Пробуем classpath: classpath:{}", cpCandidate);
            if (resource.exists()) {
                try (InputStream in = resource.getInputStream()) {
                    return in.readAllBytes();
                }
            }

            // Ничего не сработало — бросаем подробное исключение
            String msg = "Локальный ресурс не найден. raw='" + raw + "', normalized='" + normalized
                    + "'. Проверены пути: " + String.join(" | ", tried);
            throw new IOException(msg);

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}