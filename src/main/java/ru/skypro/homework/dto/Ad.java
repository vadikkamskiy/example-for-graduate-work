package ru.skypro.homework.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.skypro.homework.entity.AdImageEntity;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ad entity representing an advertisement in the system")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ad {
    @Schema(description = "Unique identifier Author", example = "1")
    Long author;
    @Schema(description = "Ad's image")
    AdImageEntity imageEntity;
    @Schema(description = "Ad's image url")
    String image;
    @Schema(description = "Ad's id", example = "2")
    Long pk;
    @Schema(description = "Price of product", example = "200")
    int price;
    @Schema(description = "Title of the ad", example = "Chair for sale")
    String title;
    @Schema(description = "Detailed description of the ad", example = "A comfortable chair in good condition")
    String description;
}
