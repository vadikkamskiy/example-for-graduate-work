package ru.skypro.homework.service;

import ru.skypro.homework.dto.User;
import ru.skypro.homework.dto.request.UpdateUserRequest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import ru.skypro.homework.dto.response.UserResponse; 
import ru.skypro.homework.dto.response.UpdateUserResponse;
import ru.skypro.homework.dto.Role;

@Service
public class UsersService {
    private final UserDetailsManager manager;
    public UsersService(UserDetailsManager manager){
        this.manager = manager;
    }

    public UserResponse getInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Principal class: " + principal.getClass().getName());
        if (principal instanceof ru.skypro.homework.dto.User) {
            ru.skypro.homework.dto.User user = (ru.skypro.homework.dto.User) principal;
            System.out.println("User: " + user.getUsername() + ", firstName: " + user.getFirstName());
            UserResponse response = new UserResponse();
            response.setUsername(user.getUsername());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setPhone(user.getPhone());
            response.setRole(user.getRole());
            return response;
        } else {
            System.out.println("Principal is not User instance, it is: " + principal.toString());
            return null;
        }
    }
    public UpdateUserResponse updateUser(UpdateUserRequest updateUser) {
        //TODO
    }
}
