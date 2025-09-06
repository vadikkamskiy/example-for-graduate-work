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

        String image = ad.getImage().getUrl();

        return new AdsResponse(
                ad.getAuthor(),
                loadLocalImage(image),
                ad.getPk(),
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

    private String loadLocalImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
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
                    return candidate.toAbsolutePath().toString();
                }
            }

            if (normalized.startsWith("file:")) {
                try {
                    URI uri = new URI(normalized);
                    Path p = Paths.get(uri);
                    tried.add(p.toAbsolutePath().toString());
                    log.debug("Пробуем file URI: {}", p.toAbsolutePath());
                    if (Files.exists(p)) {
                        return p.toAbsolutePath().toString();
                    }
                } catch (URISyntaxException ex) {
                    log.debug("Некорректный file URI: {}", normalized);
                }
            }

            Path direct = Paths.get(normalized);
            tried.add(direct.toAbsolutePath().toString());
            log.debug("Пробуем прямой путь: {}", direct.toAbsolutePath());
            if (Files.exists(direct)) {
                return direct.toAbsolutePath().toString();
            }

            Path relative = Paths.get(System.getProperty("user.dir")).resolve(trimmedForRelative);
            tried.add(relative.toAbsolutePath().toString());
            log.debug("Пробуем относительный к user.dir: {}", relative.toAbsolutePath());
            if (Files.exists(relative)) {
                return relative.toAbsolutePath().toString();
            }

            String cpCandidate = trimmedForRelative;
            Resource resource = resourceLoader.getResource("classpath:" + cpCandidate);
            tried.add("classpath:" + cpCandidate);
            log.debug("Пробуем classpath: classpath:{}", cpCandidate);
            if (resource.exists()) {
                return resource.getFile().getAbsolutePath();
            }

            String msg = "Локальный ресурс не найден. raw='" + raw + "', normalized='" + normalized
                    + "'. Проверены пути: " + String.join(" | ", tried);
            throw new IOException(msg);

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
