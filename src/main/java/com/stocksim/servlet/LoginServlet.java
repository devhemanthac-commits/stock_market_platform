package com.stocksim.servlet;

import com.google.gson.*;
import com.stocksim.data.DataStore;
import com.stocksim.model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        JsonObject body = JsonParser.parseReader(req.getReader()).getAsJsonObject();

        String username = body.get("username").getAsString().trim();
        String password = body.get("password").getAsString();

        JsonObject result = new JsonObject();
        if (DataStore.getInstance().authenticate(username, password)) {
            HttpSession session = req.getSession(true);
            session.setAttribute("username", username);
            User user = DataStore.getInstance().getUser(username);
            result.addProperty("success", true);
            result.addProperty("username", username);
            result.addProperty("fullName", user.getFullName());
        } else {
            resp.setStatus(401);
            result.addProperty("error", "Invalid username or password");
        }
        resp.getWriter().write(result.toString());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        resp.setContentType("application/json");
        resp.getWriter().write("{\"success\":true}");
    }
}
