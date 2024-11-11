package com.example.prueba.controller;
import com.example.prueba.model.Price;
import com.example.prueba.model.PriceDto;
import com.example.prueba.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    @Autowired
    private PriceService priceService;

    @GetMapping
    public ResponseEntity<PriceDto> getPrice(
            @RequestParam Long productId,
            @RequestParam Integer brandId,
            @RequestParam String dateTime) {
        try {
            LocalDateTime date = LocalDateTime.parse(dateTime);
            Price price = priceService.getPrice(productId, brandId, date);

            if (price != null) {
                PriceDto priceDto = new PriceDto(price);
                return ResponseEntity.ok(priceDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

