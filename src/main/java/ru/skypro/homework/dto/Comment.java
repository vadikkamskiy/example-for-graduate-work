package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Comment entity representing a comment in the system")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Schema(description = "Unique identifier author", example = "1")
    private Long authorId;
    @Schema(description = "Author's image")
    private String authorImage;
    @Schema(description = "Author's firstname", example = "John")
    private String authorFirstName;
    @Schema(description = "Time of create comment")
    private LocalDateTime createdAt;
    @Schema(description = "Unique identifier of the comment", example = "123")
    private Long pk;
    @Schema(description = "Text content of the comment", example = "This is a sample comment.")
    private String text;
}
