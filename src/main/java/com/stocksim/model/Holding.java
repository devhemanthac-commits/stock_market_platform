package com.stocksim.model;

import java.io.Serializable;

public class Holding implements Serializable {
    private String symbol;
    private String stockName;
    private int quantity;
    private double avgBuyPrice;

    public Holding(String symbol, String stockName, int quantity, double avgBuyPrice) {
        this.symbol = symbol;
        this.stockName = stockName;
        this.quantity = quantity;
        this.avgBuyPrice = avgBuyPrice;
    }

    public void addShares(int qty, double price) {
        double totalCost = (this.avgBuyPrice * this.quantity) + (price * qty);
        this.quantity += qty;
        this.avgBuyPrice = Math.round((totalCost / this.quantity) * 100.0) / 100.0;
    }

    public void removeShares(int qty) {
        this.quantity -= qty;
    }

    public String getSymbol() { return symbol; }
    public String getStockName() { return stockName; }
    public int getQuantity() { return quantity; }
    public double getAvgBuyPrice() { return avgBuyPrice; }
}
