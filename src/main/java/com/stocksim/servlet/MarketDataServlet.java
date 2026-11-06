package com.stocksim.servlet;

import com.google.gson.*;
import com.stocksim.data.DataStore;
import com.stocksim.model.Stock;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/api/market/*")
public class MarketDataServlet extends HttpServlet {

    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.getWriter().write(gson.toJson(
                    DataStore.getInstance().getAllStocks()));
        } else {
            String symbol = pathInfo.substring(1).toUpperCase();
            Stock stock = DataStore.getInstance().getStock(symbol);
            if (stock != null) {
                resp.getWriter().write(gson.toJson(stock));
            } else {
                resp.setStatus(404);
                resp.getWriter().write("{\"error\":\"Stock not found\"}");
            }
        }
    }
}
