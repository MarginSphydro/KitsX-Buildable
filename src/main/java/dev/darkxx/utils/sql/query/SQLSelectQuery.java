package dev.darkxx.utils.sql.query;

import com.mojang.brigadier.CommandDispatcher;
import dev.darkxx.utils.sql.enums.QueryOperation;
import java.util.Objects;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/sql/query/SQLSelectQuery.class */
public class SQLSelectQuery {
    private String tableName;
    private String whereColumn;
    private QueryOperation operation;
    private Object value;

    public static SQLSelectQuery of() {
        return new SQLSelectQuery();
    }

    public SQLSelectQuery from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SQLSelectQuery where(String column, QueryOperation operation, Object value) {
        this.whereColumn = column;
        this.operation = operation;
        this.value = value;
        return this;
    }

    public String build() {
        StringBuilder query = new StringBuilder("SELECT * FROM ");
        query.append(this.tableName);
        if (this.whereColumn != null) {
            query.append(" WHERE ").append(this.whereColumn).append(CommandDispatcher.ARGUMENT_SEPARATOR);
            if (Objects.requireNonNull(this.operation) == QueryOperation.EQUALS) {
                query.append("=").append(" '").append(this.value).append("'");
            }
        }
        return query.toString();
    }
}
