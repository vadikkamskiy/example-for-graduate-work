package ru.skypro.homework.dto.response;

import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class MyAdsResponse {
    int count;
    List<AdsResponse> results;
}
