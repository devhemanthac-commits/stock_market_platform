package com.stocksim.servlet;

import com.google.gson.*;
import com.stocksim.data.DataStore;
import com.stocksim.model.Stock;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet("/api/watchlist")
public class WatchlistServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        String username = (String) req.getSession().getAttribute("username");
        DataStore store = DataStore.getInstance();
        Set<String> symbols = store.getWatchlist(username);
        JsonArray arr = new JsonArray();
        for (String sym : symbols) {
            Stock s = store.getStock(sym);
            if (s != null) arr.add(new Gson().toJsonTree(s));
        }
        resp.getWriter().write(arr.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        String username = (String) req.getSession().getAttribute("username");
        JsonObject body = JsonParser.parseReader(req.getReader()).getAsJsonObject();
        String symbol = body.get("symbol").getAsString().toUpperCase();
        DataStore.getInstance().addToWatchlist(username, symbol);
        resp.getWriter().write("{\"success\":true}");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        String username = (String) req.getSession().getAttribute("username");
        String symbol = req.getParameter("symbol");
        if (symbol != null) {
            DataStore.getInstance().removeFromWatchlist(username, symbol.toUpperCase());
        }
        resp.getWriter().write("{\"success\":true}");
    }
}
