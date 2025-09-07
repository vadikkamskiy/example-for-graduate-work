package ru.skypro.homework.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CurrentAdResponse {
    Long pk;
    String authorFirstName;
    String authorLastName;
    String email;
    String description;
    byte[] image;
    String phone;
    int price;
    String title;
}
