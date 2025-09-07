package ru.skypro.homework.dto.response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdsResponse {
    Long author;
    String image;
    long pk;
    int price;
    String title;
}
