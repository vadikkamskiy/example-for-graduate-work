package ru.skypro.homework.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ad {
    int author;
    String image;
    int pk;
    int price;
    String title;
}
