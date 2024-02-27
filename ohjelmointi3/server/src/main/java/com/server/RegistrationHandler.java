package com.server;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class RegistrationHandler implements HttpHandler {

    private UserAuthenticator authenticator;

    public RegistrationHandler(UserAuthenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException{
        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            handleRequest(exchange);
        }
        else {
            handleResponse(exchange, 400, "Not supported\n");
        }
    }
    /*
     * Handles POST requests
     */
    public void handleRequest(HttpExchange exchange) throws IOException {

        Headers headers = exchange.getRequestHeaders();
        String contentType = "";
        JSONObject obj = null;

        try {
            if (headers.containsKey("Content-Type")) {
                contentType = headers.get("Content-Type").get(0);
                if (contentType.equalsIgnoreCase("application/json")) {
                    String newUser = new BufferedReader(new InputStreamReader(exchange.getRequestBody(),StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                    exchange.getRequestBody().close();
                    if (newUser != null && newUser.length() > 0) {
                        try {
                            obj = new JSONObject(newUser);
                        } catch (JSONException e) {
                            System.out.println(e.getMessage());
                            handleResponse(exchange, 500, "JSON parse error");
                        }
                        if (obj.getString("username").length() > 0  && obj.getString("password").length() > 0) {
                            if (authenticator.addUser(obj.getString("username"), obj.getString("password"), obj.getString("email"))) {
                                exchange.sendResponseHeaders(200, -1);
                            }
                            else {
                                handleResponse(exchange, 405, "User already registered");
                            }  
                        }
                        else {
                            handleResponse(exchange, 413, "Invalid user credentials");
                        }
                    }
                    else {
                        handleResponse(exchange, 412, "No user credentials");
                    }
                }
                else {
                    handleResponse(exchange, 407, "Content type is not application/json");
                }
            } 
            else {
                handleResponse(exchange,  411, "No content type in request");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            handleResponse(exchange, 500, "Internal server error");
        }
    }

    /*
     * Handles responses
     */
    public void handleResponse(HttpExchange exchange, Integer responseCode, String responseString) throws IOException {
        byte [] bytes = responseString.getBytes("UTF-8");
        exchange.sendResponseHeaders(responseCode, bytes.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseString.getBytes());
        outputStream.flush();
        outputStream.close(); 
    }
   
}
