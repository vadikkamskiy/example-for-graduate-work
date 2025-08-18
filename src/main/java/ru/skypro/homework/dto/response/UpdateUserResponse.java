package ru.skypro.homework.dto.response;
import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Data
public class UpdateUserResponse {
    private String firstName;
    private String lastName;
    private String phone;

}