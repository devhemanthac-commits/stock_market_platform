package com.stocksim.servlet;

import com.google.gson.*;
import com.stocksim.data.DataStore;
import com.stocksim.model.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/api/trade")
public class TradeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        String username = (String) req.getSession().getAttribute("username");
        JsonObject body = JsonParser.parseReader(req.getReader()).getAsJsonObject();

        String symbol = body.get("symbol").getAsString().toUpperCase();
        String type = body.get("type").getAsString().toUpperCase(); // BUY or SELL
        int quantity = body.get("quantity").getAsInt();

        DataStore store = DataStore.getInstance();
        Stock stock = store.getStock(symbol);
        User user = store.getUser(username);
        JsonObject result = new JsonObject();

        if (stock == null) {
            resp.setStatus(404);
            result.addProperty("error", "Stock not found");
        } else if (quantity <= 0) {
            resp.setStatus(400);
            result.addProperty("error", "Quantity must be positive");
        } else if (type.equals("BUY")) {
            double total = stock.getPrice() * quantity;
            if (user.getBalance() < total) {
                resp.setStatus(400);
                result.addProperty("error", "Insufficient funds. Need ₹" +
                        String.format("%.2f", total) + " but have ₹" +
                        String.format("%.2f", user.getBalance()));
            } else {
                user.setBalance(user.getBalance() - total);
                store.addOrUpdateHolding(username, symbol, stock.getName(),
                        quantity, stock.getPrice());
                store.addTransaction(new Transaction(username, symbol,
                        stock.getName(), "BUY", quantity, stock.getPrice()));
                result.addProperty("success", true);
                result.addProperty("message", "Bought " + quantity + " shares of " +
                        symbol + " at ₹" + String.format("%.2f", stock.getPrice()));
                result.addProperty("balance", user.getBalance());
            }
        } else if (type.equals("SELL")) {
            if (!store.reduceHolding(username, symbol, quantity)) {
                resp.setStatus(400);
                result.addProperty("error", "Insufficient shares to sell");
            } else {
                double total = stock.getPrice() * quantity;
                user.setBalance(user.getBalance() + total);
                store.addTransaction(new Transaction(username, symbol,
                        stock.getName(), "SELL", quantity, stock.getPrice()));
                result.addProperty("success", true);
                result.addProperty("message", "Sold " + quantity + " shares of " +
                        symbol + " at ₹" + String.format("%.2f", stock.getPrice()));
                result.addProperty("balance", user.getBalance());
            }
        }
        resp.getWriter().write(result.toString());
    }
}
