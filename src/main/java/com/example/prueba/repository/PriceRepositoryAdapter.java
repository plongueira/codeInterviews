package com.example.prueba.repository;


import com.example.prueba.model.Price;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final JpaPriceRepository jpaPriceRepository;

    public PriceRepositoryAdapter(JpaPriceRepository jpaPriceRepository) {
        this.jpaPriceRepository = jpaPriceRepository;
    }

    @Override
    public List<Price> findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(
            Long productId, Integer brandId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaPriceRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(
                productId, brandId, startDate, endDate);
    }
}