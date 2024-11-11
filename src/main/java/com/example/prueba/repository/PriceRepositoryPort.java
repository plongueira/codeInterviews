package com.example.prueba.repository;

import com.example.prueba.model.Price;
import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepositoryPort {
    List<Price> findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(Long productId,
            Integer brandId, LocalDateTime startDate, LocalDateTime endDate);
}
