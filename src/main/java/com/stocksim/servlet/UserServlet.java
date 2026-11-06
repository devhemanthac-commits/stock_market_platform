package com.stocksim.servlet;

import com.google.gson.*;
import com.stocksim.data.DataStore;
import com.stocksim.model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/api/user")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        String username = (String) req.getSession().getAttribute("username");
        User user = DataStore.getInstance().getUser(username);
        JsonObject result = new JsonObject();
        if (user != null) {
            result.addProperty("username", user.getUsername());
            result.addProperty("fullName", user.getFullName());
            result.addProperty("email", user.getEmail());
            result.addProperty("balance", user.getBalance());
        } else {
            resp.setStatus(404);
            result.addProperty("error", "User not found");
        }
        resp.getWriter().write(result.toString());
    }
}
