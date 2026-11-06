package com.stocksim.servlet;

import com.google.gson.*;
import com.stocksim.data.DataStore;
import com.stocksim.model.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;

@WebServlet("/api/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json");
        JsonObject body = JsonParser.parseReader(req.getReader()).getAsJsonObject();

        String username = body.get("username").getAsString().trim();
        String password = body.get("password").getAsString();
        String fullName = body.get("fullName").getAsString().trim();
        String email = body.get("email").getAsString().trim();

        JsonObject result = new JsonObject();
        if (username.isEmpty() || password.isEmpty()) {
            resp.setStatus(400);
            result.addProperty("error", "Username and password are required");
        } else {
            User user = new User(username, password, fullName, email);
            if (DataStore.getInstance().registerUser(user)) {
                HttpSession session = req.getSession(true);
                session.setAttribute("username", username);
                result.addProperty("success", true);
                result.addProperty("username", username);
                result.addProperty("fullName", fullName);
            } else {
                resp.setStatus(409);
                result.addProperty("error", "Username already taken");
            }
        }
        resp.getWriter().write(result.toString());
    }
}
