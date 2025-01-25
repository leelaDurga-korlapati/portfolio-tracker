package com.example.portfolio_tracker_backend.service;

import com.example.portfolio_tracker_backend.model.Stock;
import com.example.portfolio_tracker_backend.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.List;

@Service
public class StockService {

    @Value("${alphavantage.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private StockRepository stockRepository;

    /**
     * Fetch live stock price from Alpha Vantage API
     */
    public Double getLiveStockPrice(String ticker) {
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="
                + ticker + "&apikey=" + apiKey;
        
        System.out.println("Fetching URL: " + url);

        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("API Response: " + response);

            // Check if API limit is reached
            if (response != null && response.contains("Thank you for using Alpha Vantage")) {
                System.out.println("❌ API Limit Reached.");
                return null; // Return null if API limit is exceeded
            }

            JsonNode root = objectMapper.readTree(response);
            JsonNode globalQuote = root.path("Global Quote");

            if (globalQuote.has("05. price")) {
                return globalQuote.get("05. price").asDouble();
            } else {
                System.out.println("⚠️ Price not found in API response.");
            }
        } catch (Exception e) {
            System.err.println("🚨 Error fetching stock price: " + e.getMessage());
        }
        return null;
    }

    /**
     * Add a new stock
     */
    public Stock addStock(Stock stock) {
        return stockRepository.save(stock);
    }

    /**
     * Update an existing stock
     */
    public Stock updateStock(Long id, Stock updatedStock) {
        Optional<Stock> existingStock = stockRepository.findById(id);
        if (existingStock.isPresent()) {
            Stock stock = existingStock.get();
            stock.setName(updatedStock.getName());
            stock.setTicker(updatedStock.getTicker());
            stock.setQuantity(updatedStock.getQuantity());
            stock.setBuyPrice(updatedStock.getBuyPrice());
            return stockRepository.save(stock);
        }
        return null;
    }

    /**
     * Delete a stock by ID
     */
    public boolean deleteStock(Long id) {
        Optional<Stock> existingStock = stockRepository.findById(id);
        if (existingStock.isPresent()) {
            stockRepository.delete(existingStock.get());
            return true;
        }
        return false;
    }

    /**
     * Get all stocks
     */
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
}
