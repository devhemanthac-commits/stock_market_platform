package com.stocksim.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {
    private String id;
    private String username;
    private String symbol;
    private String stockName;
    private String type;
    private int quantity;
    private double price;
    private double total;
    private String timestamp;

    public Transaction(String username, String symbol, String stockName,
                       String type, int quantity, double price) {
        this.id = "TXN" + System.currentTimeMillis();
        this.username = username;
        this.symbol = symbol;
        this.stockName = stockName;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.total = quantity * price;
        this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getSymbol() { return symbol; }
    public String getStockName() { return stockName; }
    public String getType() { return type; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getTotal() { return total; }
    public String getTimestamp() { return timestamp; }
}
