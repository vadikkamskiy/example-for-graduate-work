package ru.skypro.homework.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Response.UserResponse;
import ru.skypro.homework.dto.request.SetPasswordRequest;
import ru.skypro.homework.dto.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody SetPasswordRequest request) {
        // Логика для установки пароля пользователя
        return ResponseEntity.ok().build();
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser() {
        // Логика для получения текущего пользователя
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateUser(@RequestBody User user) {
        // Логика для обновления текущего пользователя
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me/image")
    public ResponseEntity<Void> updateUserImage(@RequestParam String imageURL) {
        // Логика для обновления изображения пользователя
        return ResponseEntity.ok().build();
    }
}
