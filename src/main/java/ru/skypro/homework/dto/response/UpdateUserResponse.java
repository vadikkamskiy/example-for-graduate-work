package ru.skypro.homework.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserResponse {

    private String firstName;
    private String lastName;
    private String phone;
}
