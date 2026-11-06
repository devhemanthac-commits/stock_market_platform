package com.stocksim.servlet;

import com.google.gson.Gson;
import com.stocksim.data.DataStore;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/api/history")
public class HistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        String username = (String) req.getSession().getAttribute("username");
        resp.getWriter().write(new Gson().toJson(
                DataStore.getInstance().getTransactions(username)));
    }
}
