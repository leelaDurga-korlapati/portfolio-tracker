package com.example.portfolio_tracker_backend.controller;

import com.example.portfolio_tracker_backend.model.Stock;
import com.example.portfolio_tracker_backend.service.StockService;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/live-price/{ticker}")
    public ResponseEntity<?> getLivePrice(@PathVariable String ticker) {
        Double livePrice = stockService.getLiveStockPrice(ticker);

        if (livePrice == null) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("API Limit Reached. Try again later.");
        }

        return ResponseEntity.ok(livePrice);
    }

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockService.getAllStocks();
        return ResponseEntity.ok(stocks);
    }

    @PostMapping
    public ResponseEntity<?> addStock(@Valid @RequestBody Stock stock) {
        try {
            Stock savedStock = stockService.addStock(stock);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStock);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long id, @Valid @RequestBody Stock stock) {
        Stock updatedStock = stockService.updateStock(id, stock);
        return updatedStock != null ? ResponseEntity.ok(updatedStock) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        boolean deleted = stockService.deleteStock(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
