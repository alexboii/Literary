package com.example.alex.literary.mainactivity;

/**
 * Created by Alex on 8/5/2016.
 */
public class ClientCredentials {

    static final String API_KEY =
            "AIzaSyCTs9FdNF641POfi5kT9bCnApLYVbR1jxY";

    static void errorIfNotSpecified() {
        if (API_KEY.startsWith("Enter ")) {
            System.err.println(API_KEY);
            System.exit(1);
        }
    }

}
