package ru.skypro.homework.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.UsersService;
import ru.skypro.homework.dto.response.UserResponse;
import ru.skypro.homework.dto.request.SetPasswordRequest;
import ru.skypro.homework.dto.request.UpdateUserRequest;
import ru.skypro.homework.dto.response.UpdateUserResponse;
import ru.skypro.homework.dto.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("set_password")
    public ResponseEntity setPassword(@RequestBody SetPasswordRequest request) {
        if (usersService.setPassword(request.getCurrentPassword(), request.getNewPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
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
    @PatchMapping("/me")
    public ResponseEntity<UpdateUserResponse> updateUser(@RequestBody UpdateUserRequest updateUser) {
        return ResponseEntity.ok(usersService.updateUser(updateUser));
    }

    @PutMapping("/me/image")
    public ResponseEntity<Void> updateUserImage(@RequestParam String image) {
        return ResponseEntity.ok().build();
    }
}
