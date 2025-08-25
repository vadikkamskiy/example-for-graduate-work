package ru.skypro.homework.dto.request;

import lombok.Data;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Parameter;

@Data
public class CreateOrUpdateAd {
    @Size(min = 4, max = 32)
    private String title;
    @Size(min = 0, max = 10000000)
    private Integer price;
    @Size(min = 8, max = 64)
    private String description;
}
