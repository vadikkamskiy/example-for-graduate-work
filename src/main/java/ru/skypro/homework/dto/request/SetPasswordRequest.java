package ru.skypro.homework.dto.request;
import lombok.Data; 

@Data
public class SetPasswordRequest {
    private String currentPassword;
    @Size(min = 8,
     message = "New password must be at least 8 characters long",
     max = 16,
     message = "New password must be at most 16 characters long")
    private String newPassword;
}
