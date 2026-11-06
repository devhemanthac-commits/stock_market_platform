package com.stocksim.model;

import java.io.Serializable;

public class Stock implements Serializable {
    private String symbol;
    private String name;
    private String sector;
    private double price;
    private double previousClose;
    private double dayHigh;
    private double dayLow;
    private long volume;

    public Stock(String symbol, String name, String sector, double price) {
        this.symbol = symbol;
        this.name = name;
        this.sector = sector;
        this.price = price;
        this.previousClose = price;
        this.dayHigh = price;
        this.dayLow = price;
        this.volume = (long)(Math.random() * 5000000) + 100000;
    }

    public double getChange() { return price - previousClose; }
    public double getChangePercent() {
        return previousClose == 0 ? 0 : (getChange() / previousClose) * 100;
    }

    public void tick() {
        double volatility = 0.002;
        double change = price * volatility * (Math.random() * 2 - 1);
        price = Math.round((price + change) * 100.0) / 100.0;
        if (price > dayHigh) dayHigh = price;
        if (price < dayLow) dayLow = price;
        volume += (long)(Math.random() * 10000);
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public String getSector() { return sector; }
    public double getPrice() { return price; }
    public double getPreviousClose() { return previousClose; }
    public double getDayHigh() { return dayHigh; }
    public double getDayLow() { return dayLow; }
    public long getVolume() { return volume; }
    public void setPrice(double price) { this.price = price; }
}
