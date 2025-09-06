package ru.skypro.homework.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.UsersService;
import ru.skypro.homework.dto.response.UserResponse;
import ru.skypro.homework.dto.request.SetPasswordRequest;
import ru.skypro.homework.dto.request.UpdateUserRequest;
import ru.skypro.homework.dto.response.UpdateUserResponse;
import ru.skypro.homework.dto.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.RequiredArgsConstructor;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;


@Tag(name = "Users", description = "Controller for managing user profiles")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {
    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }


    @Operation(summary = "Set password", description = "Check old password and set new password")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/set_password")
    public void setPassword(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestBody SetPasswordRequest request) {
        usersService.setPassword(userDetails.getUsername(), request.getCurrentPassword(), request.getNewPassword());
    }

    @Operation(summary = "Get user information", description = "Retrieves the user's profile information")
    @GetMapping("/me")
    public UserResponse getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return usersService.getInfo(userDetails.getUsername());
    }
    @Operation(summary = "Update user information", description = "Updates the user's profile information")
    @PatchMapping("/me")
    public UserResponse updateUser(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestBody UpdateUserRequest updateUser) {
        return usersService.updateUser(userDetails.getUsername(), updateUser);
    }

    @Operation(summary = "Update user image", description = "Update the user's profile image")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/me/image")
    public void updateUserImage(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam("image") MultipartFile image) {
        usersService.updateUserImage(userDetails.getUsername(), image);
    }
}
