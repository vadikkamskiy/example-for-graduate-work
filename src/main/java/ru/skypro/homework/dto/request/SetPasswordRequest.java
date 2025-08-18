package ru.skypro.homework.dto.request;
import jakarta.validation.constraints.Size;
import lombok.Data; 

@Data
public class SetPasswordRequest {
    @Size(min = 8,max = 16)
    private String currentPassword;
    @Size(min = 8, max = 16)
    private String newPassword;
}
