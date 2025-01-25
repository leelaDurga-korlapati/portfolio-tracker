package com.example.portfolio_tracker_backend.repository;

import com.example.portfolio_tracker_backend.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    boolean existsByTicker(String ticker);
}
