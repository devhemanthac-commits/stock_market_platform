package com.stocksim.data;

import com.stocksim.model.*;
import java.util.*;
import java.util.concurrent.*;

public class DataStore {
    private static final DataStore instance = new DataStore();

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, Stock> stocks = new ConcurrentHashMap<>();
    private final Map<String, List<Holding>> holdings = new ConcurrentHashMap<>();
    private final Map<String, List<Transaction>> transactions = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> watchlists = new ConcurrentHashMap<>();

    private DataStore() {
        initStocks();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "stock-ticker");
            t.setDaemon(true);
            return t;
        });
        scheduler.scheduleAtFixedRate(this::tickAllStocks, 2, 2, TimeUnit.SECONDS);
    }

    public static DataStore getInstance() { return instance; }

    private void initStocks() {
        addStock("RELIANCE", "Reliance Industries", "Energy", 2456.80);
        addStock("TCS", "Tata Consultancy Services", "IT", 3521.45);
        addStock("HDFCBANK", "HDFC Bank Ltd", "Banking", 1642.30);
        addStock("INFY", "Infosys Ltd", "IT", 1456.90);
        addStock("ICICIBANK", "ICICI Bank Ltd", "Banking", 945.60);
        addStock("HINDUNILVR", "Hindustan Unilever", "FMCG", 2534.15);
        addStock("SBIN", "State Bank of India", "Banking", 578.25);
        addStock("BHARTIARTL", "Bharti Airtel Ltd", "Telecom", 892.40);
        addStock("ITC", "ITC Ltd", "FMCG", 438.70);
        addStock("KOTAKBANK", "Kotak Mahindra Bank", "Banking", 1756.50);
        addStock("LT", "Larsen & Toubro", "Infrastructure", 2345.80);
        addStock("AXISBANK", "Axis Bank Ltd", "Banking", 987.65);
        addStock("WIPRO", "Wipro Ltd", "IT", 412.30);
        addStock("TATAMOTORS", "Tata Motors Ltd", "Automobile", 625.40);
        addStock("SUNPHARMA", "Sun Pharmaceutical", "Pharma", 1123.75);
        addStock("MARUTI", "Maruti Suzuki India", "Automobile", 10245.60);
        addStock("TITAN", "Titan Company Ltd", "Consumer", 3187.90);
        addStock("BAJFINANCE", "Bajaj Finance Ltd", "Finance", 6892.35);
        addStock("ASIANPAINT", "Asian Paints Ltd", "Consumer", 3245.80);
        addStock("HCLTECH", "HCL Technologies", "IT", 1178.50);
    }

    private void addStock(String symbol, String name, String sector, double price) {
        stocks.put(symbol, new Stock(symbol, name, sector, price));
    }

    private void tickAllStocks() {
        stocks.values().forEach(Stock::tick);
    }

    // User operations
    public boolean registerUser(User user) {
        return users.putIfAbsent(user.getUsername(), user) == null;
    }

    public User getUser(String username) { return users.get(username); }

    public boolean authenticate(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    // Stock operations
    public Collection<Stock> getAllStocks() { return stocks.values(); }
    public Stock getStock(String symbol) { return stocks.get(symbol); }

    // Holdings operations
    public List<Holding> getHoldings(String username) {
        return holdings.getOrDefault(username, new ArrayList<>());
    }

    public synchronized void addOrUpdateHolding(String username, String symbol, String name,
                                    int qty, double price) {
        holdings.computeIfAbsent(username, k -> new ArrayList<>());
        List<Holding> userHoldings = holdings.get(username);
        for (Holding h : userHoldings) {
            if (h.getSymbol().equals(symbol)) {
                h.addShares(qty, price);
                return;
            }
        }
        userHoldings.add(new Holding(symbol, name, qty, price));
    }

    public synchronized boolean reduceHolding(String username, String symbol, int qty) {
        List<Holding> userHoldings = holdings.get(username);
        if (userHoldings == null) return false;
        Iterator<Holding> it = userHoldings.iterator();
        while (it.hasNext()) {
            Holding h = it.next();
            if (h.getSymbol().equals(symbol) && h.getQuantity() >= qty) {
                h.removeShares(qty);
                if (h.getQuantity() == 0) it.remove();
                return true;
            }
        }
        return false;
    }

    // Transaction operations
    public void addTransaction(Transaction txn) {
        transactions.computeIfAbsent(txn.getUsername(), k -> new ArrayList<>());
        transactions.get(txn.getUsername()).add(0, txn);
    }

    public List<Transaction> getTransactions(String username) {
        return transactions.getOrDefault(username, new ArrayList<>());
    }

    // Watchlist operations
    public Set<String> getWatchlist(String username) {
        return watchlists.getOrDefault(username, new LinkedHashSet<>());
    }

    public void addToWatchlist(String username, String symbol) {
        watchlists.computeIfAbsent(username, k -> new LinkedHashSet<>());
        watchlists.get(username).add(symbol);
    }

    public void removeFromWatchlist(String username, String symbol) {
        Set<String> wl = watchlists.get(username);
        if (wl != null) wl.remove(symbol);
    }
}
