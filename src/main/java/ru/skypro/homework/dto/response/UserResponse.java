package ru.skypro.homework.dto.Response;
import lombok.Data;

@Data
public class UserResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String role;

    public UserResponse(String username, String firstName, String lastName, String phone, String role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.role = role;
    }
}
