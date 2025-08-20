package ru.skypro.homework.controller;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.http.ResponseEntity;

import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.Ad;

@RestController
@RequestMapping("/ads")
public class AdsController {

    @GetMapping
    public ResponseEntity<Ads> getAds() {
        return ResponseEntity.ok(new Ads(0, List.of()));
    }

    @PostMapping
    public ResponseEntity<Ad> createAd(@RequestBody Ad ad, @RequestParam String image) {
        return ResponseEntity.ok(ad);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Ad> getAd(@PathVariable int id) {
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable int id) {
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable int id, @RequestBody Ad ad) {
        return ResponseEntity.ok(ad);
    }

    @GetMapping("/me")
    public ResponseEntity<Ads> getMyAds() {
        return ResponseEntity.ok(new Ads(0, List.of()));
    }
    @PatchMapping("/{id}/image")
    public ResponseEntity<Ad> updateAdImage(@PathVariable int id, @RequestBody String image) {
        return ResponseEntity.ok().build();
    }
}
