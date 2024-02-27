package com.server;

import java.sql.SQLException;
import org.json.JSONException;
import org.json.JSONObject;
import com.sun.net.httpserver.BasicAuthenticator;

public class UserAuthenticator extends BasicAuthenticator {

    private static final MessageDatabase database = MessageDatabase.getInstance();

    public UserAuthenticator(String realm) {
        super(realm); 
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        try {
            return database.authenticateUser(username, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean addUser(String username, String password, String email) {
        try {
            if (!database.existsUser(username)) {
                try {  
                    JSONObject newUser = new JSONObject();
                    newUser.put("username", username);
                    newUser.put("password", password);
                    newUser.put("email", email);
                    database.setUser(newUser);
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
                
            }
            return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}
