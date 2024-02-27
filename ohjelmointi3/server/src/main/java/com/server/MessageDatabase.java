package com.server;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.sql.DriverManager;
import java.sql.ResultSet;
import org.apache.commons.codec.digest.Crypt;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.security.SecureRandom;

public class MessageDatabase {
    
    private Connection connection = null;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static volatile MessageDatabase dbInstance;
    private static Object mutex = new Object();

	public static MessageDatabase getInstance() {
        MessageDatabase resultInstance = dbInstance;
		if (resultInstance == null) {
            synchronized (mutex) {
                resultInstance = dbInstance;
                if (resultInstance == null) {
                    dbInstance = resultInstance = new MessageDatabase();
                }
            }
		}
        return resultInstance;
    }

    private MessageDatabase() {
    }

    public void open(String dbName) throws SQLException {
        File f = new File(dbName);
        if (!f.exists()) {
            String database = "jdbc:sqlite:" + dbName;
            connection = DriverManager.getConnection(database);
            initializeDatabase();
        }
        
    }

    public void closeDatabase() throws SQLException {
        if (connection != null) {
			connection.close();
			connection = null;
		}
    }

    private boolean initializeDatabase() throws SQLException {
        if (connection != null) {
            String createUsersString = "CREATE TABLE Users ("
            + "username VARCHAR(50) NOT NULL PRIMARY KEY, "
            + "password VARCHAR(50) NOT NULL, "
            + "email VARCHAR(50))"; 
            String createMessagesString = "CREATE TABLE Messages ("
            + "id INTEGER PRIMARY KEY, "
            + "username VARCHAR(50) NOT NULL, "
            + "nickname VARCHAR(50) NOT NULL, "
            + "longitude DOUBLE NOT NULL, "
            + "latitude DOUBLE NOT NULL, "
            + "sent LONG NOT NULL, "
            + "dangertype VARCHAR(50) NOT NULL, "
            + "areacode VARCHAR(50), "
            + "phonenumber VARCHAR(50), "
            + "updatereason VARCHAR(50), "
            + "modified LONG)";
            Statement createStatement = connection.createStatement();
            createStatement.executeUpdate(createUsersString);
            createStatement.executeUpdate(createMessagesString);
            createStatement.close();
            return true;
        }
        return false;
    } 

    public void newMessage(JSONObject warningMessage) throws SQLException {
        String insertNewMessage = "INSERT INTO Messages " 
        + "(username, "
        + "nickname, "
        + "longitude, "
        + "latitude, "
        + "sent, "
        + "dangertype, "
        + "areacode, "
        + "phonenumber, "
        + "updatereason, "
        + "modified) "
        + "VALUES('"
        + warningMessage.getString("username")
        + "','"
        + warningMessage.getString("nickname")
        + "','"
        + warningMessage.getDouble("longitude")
        + "','"
        + warningMessage.getDouble("latitude")
        + "','"
        + warningMessage.getLong("sent")
        + "','"
        + warningMessage.getString("dangertype")
        + "','"
        + warningMessage.getString("areacode")
        + "','"
        + warningMessage.getString("phonenumber")
        + "','"
        + warningMessage.getString("updatereason")
        + "','"
        + warningMessage.getLong("modified")
        + "')";
        Statement createStatement = connection.createStatement();
        createStatement.executeUpdate(insertNewMessage);
        createStatement.close();
    }

    public void updateMessage(JSONObject warningMessage) throws SQLException {
        String updateMessage = "UPDATE Messages " 
        + "SET nickname = '" + warningMessage.getString("nickname")
        + "', longitude = " + warningMessage.getDouble("longitude")
        + ", latitude = " + warningMessage.getDouble("latitude")
        + ", sent = " + warningMessage.getLong("sent")
        + ", dangertype = '" + warningMessage.getString("dangertype")
        + "', areacode = '" + warningMessage.getString("areacode")
        + "', phonenumber = '" + warningMessage.getString("phonenumber")
        + "', updatereason = '" + warningMessage.getString("updatereason")
        + "', modified = " + warningMessage.getLong("modified")
        + " WHERE id = " + warningMessage.getInt("id"); 
        Statement createStatement = connection.createStatement();
        createStatement.executeUpdate(updateMessage);
        createStatement.close();
    }

    /*
     * Get every message from table as a JSONArray
     */
    public JSONArray getMessages() throws SQLException {
        JSONArray result = new JSONArray();
        // Select every field except username
        String getMessagesString = "SELECT * FROM Messages";
        Statement queryStatement = connection.createStatement();
        ResultSet rs = queryStatement.executeQuery(getMessagesString);

        while (rs.next()) {
            JSONObject obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("nickname", rs.getString("nickname"));
            obj.put("longitude", rs.getDouble("longitude"));
            obj.put("latitude", rs.getDouble("latitude"));
            obj.put("sent", dateAsZDT(rs.getLong("sent")));
            obj.put("dangertype", rs.getString("dangertype"));
            // Check for optional fields
            if (!rs.getString("areacode").equals("nodata")) {
                obj.put("areacode", rs.getString("areacode"));
            }
            if (!rs.getString("phonenumber").equals("nodata")) {
                obj.put("phonenumber", rs.getString("phonenumber"));
            }
            if (rs.getLong("modified") != 0) {
                obj.put("updatereason", rs.getString("updatereason"));
                obj.put("modified", dateAsZDT(rs.getLong("modified")));
            }
            result.put(obj);
        }
        return result;
    }

    public boolean validateEdit(int id, String username) throws SQLException {
        String query = "SELECT id, username FROM Messages WHERE id = '" + id + "'";
        Statement queryStatement = connection.createStatement();
        ResultSet rs = queryStatement.executeQuery(query);
        if (rs.next()) {
            if (username.equals(rs.getString("username"))) {
                return true;
            }
        }
        return false;
    }

    public JSONArray queryMessages(JSONObject obj) throws SQLException {
        JSONArray result = new JSONArray();
        String query = "";
        // Check whether querying for time or user
        if (obj.getString("query").equalsIgnoreCase("time")) {
            query = "SELECT * FROM Messages "
            + "WHERE sent > " + obj.getLong("timestart") + " AND sent < " + obj.getLong("timeend");
        }
        else if (obj.getString("query").equalsIgnoreCase("location")) {
            query = "SELECT * FROM Messages "
            + "WHERE longitude BETWEEN " + obj.getDouble("uplongitude") + " AND " + obj.getDouble("downlongitude")
            + " AND latitude BETWEEN " + obj.getDouble("downlatitude") + " AND " + obj.getDouble("uplatitude");
        }
        else {
            query = "SELECT * FROM Messages "
            + "WHERE nickname = '"+ obj.getString("nickname") + "'";
        }
        Statement queryStatement = connection.createStatement();
        ResultSet rs = queryStatement.executeQuery(query);
        while (rs.next()) {
            obj = new JSONObject();
            obj.put("id", rs.getInt("id"));
            obj.put("nickname", rs.getString("nickname"));
            obj.put("longitude", rs.getDouble("longitude"));
            obj.put("latitude", rs.getDouble("latitude"));
            obj.put("sent", dateAsZDT(rs.getLong("sent")));
            obj.put("dangertype", rs.getString("dangertype"));
            // Check for optional fields
            if (!rs.getString("areacode").equals("nodata")) {
                obj.put("areacode", rs.getString("areacode"));
            }
            if (!rs.getString("phonenumber").equals("nodata")) {
                obj.put("phonenumber", rs.getString("phonenumber"));
            }
            if (rs.getLong("modified") != 0) {
                obj.put("updatereason", rs.getString("updatereason"));
                obj.put("modified", dateAsZDT(rs.getLong("modified")));
            }
            result.put(obj);
        }
        return result;
    }

    public void setUser(JSONObject user) throws SQLException {
        byte bytes[] = new byte[13];
        secureRandom.nextBytes(bytes); 
        String saltBytes = new String(Base64.getEncoder().encode(bytes));
        String salt = "$6$" + saltBytes; 
        String password = user.getString("password");
        String hashedPassword = Crypt.crypt(password, salt); 
        String insertNewUser = "INSERT INTO Users " 
        + "VALUES('"
        + user.getString("username")
        + "','"
        + hashedPassword
        + "','"
        + user.getString("email")
        + "')";
        Statement createStatement = connection.createStatement();
        createStatement.executeUpdate(insertNewUser);
        createStatement.close();
    }

    public boolean existsUser(String username) throws SQLException {
        String getUserString = "SELECT username FROM Users WHERE username = '" + username + "'";
        Statement queryStatement = connection.createStatement();
        ResultSet rs = queryStatement.executeQuery(getUserString);
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public boolean authenticateUser(String username, String password) throws SQLException {
        String getUserString = "SELECT password FROM Users WHERE username = '" + username + "'";
        Statement queryStatement = connection.createStatement();
        ResultSet rs = queryStatement.executeQuery(getUserString);
        if (rs.next()) {
            String hashedPassword = rs.getString("password");
            if (hashedPassword.equals(Crypt.crypt(password, hashedPassword))) {
                return true;
            }
        }
        return false;
    }

    public ZonedDateTime dateAsZDT(long epoch) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneOffset.UTC);
    }
}
