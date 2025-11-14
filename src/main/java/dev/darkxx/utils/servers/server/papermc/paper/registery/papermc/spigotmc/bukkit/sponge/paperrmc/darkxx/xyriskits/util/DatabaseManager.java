package dev.darkxx.utils.servers.server.papermc.paper.registery.papermc.spigotmc.bukkit.sponge.paperrmc.darkxx.xyriskits.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/servers/server/papermc/paper/registery/papermc/spigotmc/bukkit/sponge/paperrmc/darkxx/xyriskits/util/DatabaseManager.class */
public class DatabaseManager {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private Connection connection;

    public Connection getConnection() {
        return this.connection;
    }

    public DatabaseManager(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public void connect() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
        this.connection = DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPassword);
    }

    public void ensureConnection() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            connect();
        }
    }

    public void closeConnection() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }
}
