package dev.darkxx.utils.sql.query;

import com.mojang.brigadier.CommandDispatcher;
import dev.darkxx.utils.sql.enums.QueryOperation;
import java.util.Objects;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/sql/query/SQLDeleteQuery.class */
public class SQLDeleteQuery {
    private String tableName;
    private String whereColumn;
    private QueryOperation operation;
    private Object value;

    public static SQLDeleteQuery of() {
        return new SQLDeleteQuery();
    }

    public SQLDeleteQuery from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SQLDeleteQuery where(String column, QueryOperation operation, Object value) {
        this.whereColumn = column;
        this.operation = operation;
        this.value = value;
        return this;
    }

    public String build() {
        StringBuilder query = new StringBuilder("DELETE FROM ");
        query.append(this.tableName).append(" WHERE ").append(this.whereColumn).append(CommandDispatcher.ARGUMENT_SEPARATOR);
        if (Objects.requireNonNull(this.operation) == QueryOperation.EQUALS) {
            query.append("=").append(" '").append(this.value).append("'");
        }
        return query.toString();
    }
}
