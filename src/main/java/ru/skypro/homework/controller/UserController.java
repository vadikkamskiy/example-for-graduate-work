package ru.skypro.homework.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.UsersService;
import ru.skypro.homework.dto.response.UserResponse;
import ru.skypro.homework.dto.request.SetPasswordRequest;
import ru.skypro.homework.dto.request.UpdateUserRequest;
import ru.skypro.homework.dto.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/set_password")
    public ResponseEntity<UserResponse> setPassword(@RequestBody SetPasswordRequest request) {
        // Логика для установки пароля пользователя
        return ResponseEntity.ok().build();
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser() {
    UserResponse userResponse = usersService.getInfo();
    if (userResponse != null) {
        return ResponseEntity.ok(userResponse);
    } else {
        return ResponseEntity.notFound().build();
    }
}
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UpdateUserRequest updateUser) {
        // Логика для обновления текущего пользователя
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/image")
    public ResponseEntity<Void> updateUserImage(@RequestParam String imageURL) {
        // Логика для обновления изображения пользователя
        return ResponseEntity.ok().build();
    }
}
