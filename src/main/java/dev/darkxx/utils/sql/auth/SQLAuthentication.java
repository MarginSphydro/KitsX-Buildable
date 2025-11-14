package dev.darkxx.utils.sql.auth;

import dev.darkxx.utils.sql.connection.SQLConnector;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/sql/auth/SQLAuthentication.class */
public class SQLAuthentication {
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public static SQLAuthentication of() {
        return new SQLAuthentication();
    }

    public SQLAuthentication host(String host) {
        this.host = host;
        return this;
    }

    public SQLAuthentication port(int port) {
        this.port = port;
        return this;
    }

    public SQLAuthentication database(String database) {
        this.database = database;
        return this;
    }

    public SQLAuthentication username(String username) {
        this.username = username;
        return this;
    }

    public SQLAuthentication password(String password) {
        this.password = password;
        return this;
    }

    public SQLConnector build() {
        return new SQLConnector(this);
    }

    public String host() {
        return this.host;
    }

    public int port() {
        return this.port;
    }

    public String database() {
        return this.database;
    }

    public String username() {
        return this.username;
    }

    public String password() {
        return this.password;
    }
}
