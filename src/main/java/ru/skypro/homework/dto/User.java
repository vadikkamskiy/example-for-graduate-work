package ru.skypro.homework.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import ru.skypro.homework.dto.Role;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
public class User{
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;

}
