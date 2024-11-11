package com.example.prueba.service;

import com.example.prueba.model.Price;
import com.example.prueba.repository.PriceRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;


@Service
public class PriceService {

    @Autowired
    private final PriceRepositoryPort priceRepositoryPort;
    @Autowired
    public PriceService(PriceRepositoryPort priceRepositoryPort) {
        this.priceRepositoryPort = priceRepositoryPort;
    }

    public Price getPrice(Long productId, Integer brandId, LocalDateTime dateTime) {
        if (productId == null || brandId == null || dateTime == null) {
            throw new IllegalArgumentException("Product ID, Brand ID, and Date must not be null");
        }

        // Buscar precios aplicables
        List<Price> applicablePrices = findApplicablePrices(productId, brandId, dateTime);

        // Seleccionar la tarifa de mayor prioridad
        return selectHighestPriorityPrice(applicablePrices);
    }

    private List<Price> findApplicablePrices(Long productId, Integer brandId, LocalDateTime dateTime) {
        return priceRepositoryPort.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(productId, brandId, dateTime, dateTime);
    }

    private Price selectHighestPriorityPrice(List<Price> prices) {
        return prices.stream()
                .max(Comparator.comparing(Price::getPriority))
                .orElse(null);
    }
}
