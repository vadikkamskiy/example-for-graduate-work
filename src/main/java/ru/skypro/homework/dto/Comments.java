package ru.skypro.homework.dto;

import ru.skypro.homework.dto.Comment;
import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Comments {
    private int count;
    private List<Comment> results;
}
