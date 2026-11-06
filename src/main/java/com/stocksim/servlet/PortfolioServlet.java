package com.stocksim.servlet;

import com.google.gson.*;
import com.stocksim.data.DataStore;
import com.stocksim.model.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet("/api/portfolio")
public class PortfolioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        String username = (String) req.getSession().getAttribute("username");
        DataStore store = DataStore.getInstance();

        User user = store.getUser(username);
        List<Holding> userHoldings = store.getHoldings(username);

        JsonObject result = new JsonObject();
        result.addProperty("balance", user != null ? user.getBalance() : 0.0);

        double investedValue = 0, currentValue = 0;
        JsonArray holdingsArr = new JsonArray();

        for (Holding h : userHoldings) {
            Stock stock = store.getStock(h.getSymbol());
            JsonObject hObj = new JsonObject();
            hObj.addProperty("symbol", h.getSymbol());
            hObj.addProperty("name", h.getStockName());
            hObj.addProperty("quantity", h.getQuantity());
            hObj.addProperty("avgPrice", h.getAvgBuyPrice());
            hObj.addProperty("currentPrice", stock != null ? stock.getPrice() : 0);
            double invested = h.getAvgBuyPrice() * h.getQuantity();
            double current = (stock != null ? stock.getPrice() : 0) * h.getQuantity();
            hObj.addProperty("invested", invested);
            hObj.addProperty("currentValue", current);
            hObj.addProperty("pnl", current - invested);
            hObj.addProperty("pnlPercent",
                    invested > 0 ? ((current - invested) / invested) * 100 : 0);
            holdingsArr.add(hObj);
            investedValue += invested;
            currentValue += current;
        }

        result.add("holdings", holdingsArr);
        result.addProperty("totalInvested", investedValue);
        result.addProperty("totalCurrentValue", currentValue);
        result.addProperty("totalPnl", currentValue - investedValue);
        result.addProperty("totalPnlPercent",
                investedValue > 0 ? ((currentValue - investedValue) / investedValue) * 100 : 0);
        resp.getWriter().write(result.toString());
    }
}
