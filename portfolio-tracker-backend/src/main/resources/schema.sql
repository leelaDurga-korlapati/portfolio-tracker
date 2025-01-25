CREATE DATABASE portfolio_db;

USE portfolio_db;

CREATE TABLE stock (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    ticker VARCHAR(10) NOT NULL,
    quantity INT NOT NULL,
    buy_price DECIMAL(10,2) NOT NULL
);
