package com.example.prueba.service;

import com.example.prueba.model.Price;

import com.example.prueba.repository.PriceRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
class PriceServiceTest {

    @Mock
    private PriceRepositoryPort priceRepository;

    @InjectMocks
    private PriceService priceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetPriceWithHighestPriority() {
        LocalDateTime dateTime = LocalDateTime.parse("2020-06-14T16:00:00");

        Price price1 = new Price(35455L, 1, LocalDateTime.of(2020, 6, 14, 15, 0), LocalDateTime.of(2020, 6, 14, 18, 30), 2, 1, 25.45, "EUR");
        Price price2 = new Price(35455L, 1, LocalDateTime.of(2020, 6, 14, 0, 0), LocalDateTime.of(2020, 12, 31, 23, 59), 1, 0, 35.50, "EUR");

        when(priceRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(35455L, 1, dateTime, dateTime))
                .thenReturn(Arrays.asList(price1, price2));

        Price result = priceService.getPrice(35455L, 1, dateTime);

        assertEquals(price1, result);
    }

    @Test
    void testGetPriceNoPriceFound() {
        LocalDateTime dateTime = LocalDateTime.parse("2020-06-14T10:00:00");

        when(priceRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(35455L, 1, dateTime, dateTime))
                .thenReturn(Arrays.asList());

        Price result = priceService.getPrice(35455L, 1, dateTime);

        assertNull(result);
    }

    @Test
    void testGetPricePriceInRange() {
        LocalDateTime dateTime = LocalDateTime.parse("2020-06-14T10:00:00");

        Price price1 = new Price(35455L, 1, LocalDateTime.of(2020, 6, 14, 9, 0), LocalDateTime.of(2020, 6, 14, 12, 0), 2, 1, 25.45, "EUR");

        when(priceRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(35455L, 1, dateTime, dateTime))
                .thenReturn(Arrays.asList(price1));

        Price result = priceService.getPrice(35455L, 1, dateTime);

        assertEquals(price1, result);
    }

    @Test
    void testGetPricePriceExactStartDate() {
        LocalDateTime dateTime = LocalDateTime.parse("2020-06-14T10:00:00");

        Price price1 = new Price(35455L, 1, LocalDateTime.of(2020, 6, 14, 10, 0), LocalDateTime.of(2020, 6, 14, 18, 0), 1, 0, 35.50, "EUR");

        when(priceRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(35455L, 1, dateTime, dateTime))
                .thenReturn(Arrays.asList(price1));

        Price result = priceService.getPrice(35455L, 1, dateTime);

        assertEquals(price1, result);
    }

    @Test
    void testGetPricePriceEndDateMatch() {
        LocalDateTime dateTime = LocalDateTime.parse("2020-06-14T10:00:00");

        Price price1 = new Price(35455L, 1, LocalDateTime.of(2020, 6, 14, 9, 0), LocalDateTime.of(2020, 6, 14, 10, 0), 1, 0, 35.50, "EUR");

        when(priceRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(35455L, 1, dateTime, dateTime))
                .thenReturn(Arrays.asList(price1));

        Price result = priceService.getPrice(35455L, 1, dateTime);

        assertEquals(price1, result);
    }

    @Test
    void testGetPriceWithMultiplePrices() {
        LocalDateTime dateTime = LocalDateTime.parse("2020-06-14T10:00:00");

        Price price1 = new Price(35455L, 1, LocalDateTime.of(2020, 6, 14, 9, 0), LocalDateTime.of(2020, 6, 14, 12, 0), 1, 0, 35.50, "EUR");
        Price price2 = new Price(35455L, 1, LocalDateTime.of(2020, 6, 14, 10, 0), LocalDateTime.of(2020, 6, 14, 18, 0), 2, 1, 25.45, "EUR");

        when(priceRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(35455L, 1, dateTime, dateTime))
                .thenReturn(Arrays.asList(price1, price2));

        Price result = priceService.getPrice(35455L, 1, dateTime);

        assertEquals(price2, result);
    }
}