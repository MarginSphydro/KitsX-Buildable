package dev.darkxx.utils.sql.connection;

import dev.darkxx.utils.sql.auth.SQLAuthentication;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/sql/connection/SQLConnector.class */
public final class SQLConnector {
    private final SQLAuthentication authentication;

    public SQLConnector(SQLAuthentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public final String toString() {
        return "SQLConnector[authentication=" + this.authentication + "]";
    }

    @Override
    public final int hashCode() {
        return java.util.Objects.hashCode(this.authentication);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLConnector)) return false;
        SQLConnector that = (SQLConnector) o;
        return java.util.Objects.equals(this.authentication, that.authentication);
    }

    public SQLAuthentication authentication() {
        return this.authentication;
    }

    public static SQLConnector of() {
        SQLAuthentication authentication = SQLAuthentication.of().database("database").host("localhost").password("").username("root").port(3306);
        return new SQLConnector(authentication);
    }

    public static SQLConnector of(SQLAuthentication authentication) {
        return new SQLConnector(authentication);
    }

    public Connection connect() {
        String url = "jdbc:mysql://" + this.authentication.host() + ":" + this.authentication.port() + "/" + this.authentication.database();
        try {
            return DriverManager.getConnection(url, this.authentication.username(), this.authentication.password());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
