package ru.skypro.homework.dto.response;
import ru.skypro.homework.dto.Role;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private byte[] image;

    public UserResponse(String username, String firstName, String lastName, String phone, Role role, byte[] image) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
        this.image = image;
    }
}
