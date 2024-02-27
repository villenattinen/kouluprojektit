package com.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class WarningHandler implements HttpHandler {

    private static final MessageDatabase database = MessageDatabase.getInstance();

    public WarningHandler() {
    }
    
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            handleGetRequest(exchange);
        } 
        else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            handlePostRequest(exchange);
        } 
        else {
            handleResponse(exchange, 400, "Not supported");
        }
    }

    /*
     * Handles GET requests
     */
    private void handleGetRequest(HttpExchange exchange) throws IOException {
        try {
            JSONArray responseMessages = database.getMessages();
            if (!responseMessages.equals(null)) {
                String response = responseMessages.toString().strip();
                handleResponse(exchange, 200, response);
            } 
            else {
                exchange.sendResponseHeaders(204, -1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            handleResponse(exchange, 500, "Internal server error");
        }
    }

    /*
     * Handles POST requests
     */
    private void handlePostRequest(HttpExchange exchange) throws IOException {

        Headers headers = exchange.getRequestHeaders();
        String username = exchange.getPrincipal().getUsername();
        String contentType = "";
        JSONObject obj = null;

        try {
            if (headers.containsKey("Content-Type")) { 
                contentType = headers.get("Content-Type").get(0);
                if (contentType.equalsIgnoreCase("application/json")) {
                    String newRequest = new BufferedReader(new InputStreamReader(exchange.getRequestBody(),StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
                    exchange.getRequestBody().close();
                    if (newRequest != null && newRequest.length() > 0) {
                        try {
                            obj = new JSONObject(newRequest);
                            if (!obj.has("query")) {
                                String dangertype = obj.getString("dangertype");
                                if (dangertype.equalsIgnoreCase("moose") || dangertype.equalsIgnoreCase("reindeer") || dangertype.equalsIgnoreCase("deer") || dangertype.equalsIgnoreCase("other")) {
                                    long sent = dateAsLong(obj.getString("sent"));
                                    obj.put("username", username);                    
                                    obj.remove("sent");
                                    obj.put("sent", sent);
                                    if (!obj.has("areacode")) {
                                        obj.put("areacode", "nodata");
                                    }
                                    if (!obj.has("phonenumber")) {
                                        obj.put("phonenumber", "nodata");
                                    }
                                    if (!obj.has("id")) {
                                        obj.put("updatereason", "");
                                        obj.put("modified", 0);
                                        database.newMessage(obj);
                                    }
                                    else if (database.validateEdit(obj.getInt("id"), username)) {
                                        obj.put("modified", dateAsLong(getTime()));
                                        database.updateMessage(obj);
                                    }
                                    exchange.sendResponseHeaders(200, -1);
                                } else {
                                    handleResponse(exchange, 422, "Invalid dangertype");
                                }
                            }
                            else if (obj.getString("query").equalsIgnoreCase("time")) {
                                long timeStart = dateAsLong(obj.getString("timestart"));
                                long timeEnd = dateAsLong(obj.getString("timeend"));
                                obj.remove("timestart");
                                obj.remove("timeend");
                                obj.put("timestart", timeStart);
                                obj.put("timeend", timeEnd);
                                JSONArray responseMessages = database.queryMessages(obj);
                                if (!responseMessages.equals(null)) {
                                    String response = responseMessages.toString().strip();
                                    handleResponse(exchange, 200, response);
                                } 
                                else {
                                    exchange.sendResponseHeaders(204, -1);
                                }
                            }
                            else if (obj.getString("query").equalsIgnoreCase("user")) {
                                JSONArray responseMessages = database.queryMessages(obj);
                                if (!responseMessages.equals(null)) {
                                    String response = responseMessages.toString().strip();
                                    handleResponse(exchange, 200, response);
                                } 
                                else {
                                    exchange.sendResponseHeaders(204, -1);
                                }
                            }
                            else if (obj.getString("query").equalsIgnoreCase("location")) {
                                JSONArray responseMessages = database.queryMessages(obj);
                                if (!responseMessages.equals(null)) {
                                    String response = responseMessages.toString().strip();
                                    handleResponse(exchange, 200, response);
                                } 
                                else {
                                    exchange.sendResponseHeaders(204, -1);
                                }
                            }
                            else {
                                handleResponse(exchange, 406, "Invalid query method");
                            }
                        } catch (JSONException e) {
                            System.out.println(e.getMessage());
                            handleResponse(exchange, 500, "JSON parse error");
                        } catch (DateTimeParseException e) {
                            System.out.println(e.getMessage());
                            handleResponse(exchange, 500, "DateTime parse error");           
                        }
                    }
                    else {
                        handleResponse(exchange, 412, "Empty message");
                    }
                }
                else {
                    handleResponse(exchange, 407, "Content type is not application/json");                    
                }
            }
            else {
                handleResponse(exchange, 411, "No content type in request");
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

    /*
     * Date time conversion methods
     */
    public long dateAsLong(String dateTimeString) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSSX"));
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public String getTime() {
        ZonedDateTime now =ZonedDateTime.now(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSSX");
        return now.format(formatter);
    }
    
}
