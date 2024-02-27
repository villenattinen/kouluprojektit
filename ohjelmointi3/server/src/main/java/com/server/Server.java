package com.server;

import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import com.sun.net.httpserver.*;
import java.io.*;

public class Server {

    private Server() {
    }

    private static SSLContext serverSSLContext(String keystorePath, String password) throws Exception {
        char[] passphrase = password.toCharArray(); 
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keystorePath), passphrase); 
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);
     
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);
     
        SSLContext ssl = SSLContext.getInstance("TLS");
        ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return ssl;
    }

    public static void main(String[] args) throws Exception {
        try {
            //create the http server to port 8001 with default logger
            HttpsServer server = HttpsServer.create(new InetSocketAddress(8001),0);
            SSLContext sslContext = serverSSLContext(args[0], args[1]);
            server.setHttpsConfigurator (new HttpsConfigurator(sslContext) {
                public void configure (HttpsParameters params) {
                InetSocketAddress remote = params.getClientAddress();
                SSLContext c = getSSLContext();
                SSLParameters sslparams = c.getDefaultSSLParameters();
                params.setSSLParameters(sslparams);
                }
            });
            MessageDatabase.getInstance().open("messagedatabase.db");
            UserAuthenticator authenticator = new UserAuthenticator("warning");
            //create context that defines path for the resource, in this case a "help"
            HttpContext serverWarningContext = server.createContext("/warning", new WarningHandler());
            server.createContext("/registration", new RegistrationHandler(authenticator));
            serverWarningContext.setAuthenticator(authenticator);
            // creates a default executor
            server.setExecutor(Executors.newCachedThreadPool());
            server.start(); 
        } catch (FileNotFoundException e) {
            // Certificate file not found!
            System.out.println("Certificate not found!");
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

}
