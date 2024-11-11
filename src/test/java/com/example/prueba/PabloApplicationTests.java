package com.example.prueba;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import com.example.prueba.controller.PriceController;
import com.example.prueba.model.Price;
import com.example.prueba.service.PriceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;

@WebMvcTest(controllers = PriceController.class)
class PabloApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PriceService priceService;

	// Test parametrizado para reducir redundancia
	@ParameterizedTest
	@CsvSource({
			"2020-06-14T10:00:00, 35455, 1, 35.50, EUR",
			"2020-06-14T16:00:00, 35455, 1, 25.45, EUR",
			"2020-06-14T21:00:00, 35455, 1, 35.50, EUR",
			"2020-06-15T10:00:00, 35455, 1, 30.50, EUR",
			"2020-06-16T21:00:00, 35455, 1, 38.95, EUR"
	})
	void testGetPrice(String dateTime, Long productId, Integer brandId, Double expectedPrice, String expectedCurrency) throws Exception {
		LocalDateTime date = LocalDateTime.parse(dateTime);

		// Margen de 2 horas para encontrar objeto esperado
		Price price = createPrice(productId, brandId, date.minusHours(1), date.plusHours(1), 1, expectedPrice, expectedCurrency);
		when(priceService.getPrice(productId, brandId, date)).thenReturn(price);

		// Realización de la petición y verificación de la respuesta
		performAndVerifyPriceRequest(dateTime, productId, brandId, expectedPrice, expectedCurrency);
	}
	@Test
	void testGetPriceNotFound() throws Exception {
		String dateTime = "2020-06-14T10:00:00";
		when(priceService.getPrice(35455L, 1, LocalDateTime.parse(dateTime))).thenReturn(null);

		mockMvc.perform(get("/api/prices")
						.param("productId", "35455")
						.param("brandId", "1")
						.param("dateTime", dateTime))
				.andExpect(status().isNotFound());
	}

	@Test
	void testGetPriceInvalidDateFormat() throws Exception {
		String invalidDateTime = "2020-06-14"; // Formato incorrecto

		mockMvc.perform(get("/api/prices")
						.param("productId", "35455")
						.param("brandId", "1")
						.param("dateTime", invalidDateTime))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testGetPriceInvalidProductId() throws Exception {
		String dateTime = "2020-06-14T10:00:00";
		mockMvc.perform(get("/api/prices")
						.param("productId", "invalid")
						.param("brandId", "1")
						.param("dateTime", dateTime))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testGetPriceInvalidBrandId() throws Exception {
		String dateTime = "2020-06-14T10:00:00";
		mockMvc.perform(get("/api/prices")
						.param("productId", "35455")
						.param("brandId", "invalid")
						.param("dateTime", dateTime))
				.andExpect(status().isBadRequest());
	}
	@Test
	void testGetPriceExactStartDateMatch() throws Exception {
		String dateTime = "2020-06-14T10:00:00";  // coincide con la fecha de inicio
		Price price = createPrice(35455L, 1, LocalDateTime.of(2020, 6, 14, 10, 0), LocalDateTime.of(2020, 6, 14, 18, 0), 1, 35.50, "EUR");

		when(priceService.getPrice(35455L, 1, LocalDateTime.parse(dateTime))).thenReturn(price);

		mockMvc.perform(get("/api/prices")
						.param("productId", "35455")
						.param("brandId", "1")
						.param("dateTime", dateTime))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.price").value(35.50))
				.andExpect(jsonPath("$.currency").value("EUR"));
	}


	private void performAndVerifyPriceRequest(String dateTime, Long productId, Integer brandId, Double expectedPrice, String expectedCurrency) throws Exception {
		mockMvc.perform(get("/api/prices")
						.param("productId", String.valueOf(productId))
						.param("brandId", String.valueOf(brandId))
						.param("dateTime", dateTime))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.price").value(expectedPrice))
				.andExpect(jsonPath("$.currency").value(expectedCurrency));
	}

	private Price createPrice(Long productId, int brandId, LocalDateTime startDate, LocalDateTime endDate, int priceList, double price, String currency) {
		return new Price(productId, brandId, startDate, endDate, priceList, 0, price, currency);
	}
}
