package com.example.prueba.repository;

import com.example.prueba.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaPriceRepository extends JpaRepository<Price, Long> {

    List<Price> findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(
            Long productId, Integer brandId, LocalDateTime startDate, LocalDateTime endDate);
}