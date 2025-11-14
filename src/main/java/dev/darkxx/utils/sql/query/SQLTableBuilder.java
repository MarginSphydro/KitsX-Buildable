package dev.darkxx.utils.sql.query;

import com.mojang.brigadier.CommandDispatcher;
import dev.darkxx.utils.sql.enums.PrimaryColumn;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.runtime.ObjectMethods;
import java.util.ArrayList;
import java.util.List;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/sql/query/SQLTableBuilder.class */
public class SQLTableBuilder {
    private String tableName;
    private final List<Column> columns = new ArrayList();

    public static SQLTableBuilder of() {
        return new SQLTableBuilder();
    }

    public SQLTableBuilder name(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SQLTableBuilder column(String name, String type, PrimaryColumn isPrimary) {
        Column column = new Column(name, type, isPrimary);
        this.columns.add(column);
        return this;
    }

    public SQLTableBuilder column(String name, String type) {
        return column(name, type, PrimaryColumn.FALSE);
    }

    public SQLTableBuilder column(String name, String type, boolean isPrimary) {
        return isPrimary ? column(name, type, PrimaryColumn.TRUE) : column(name, type, PrimaryColumn.FALSE);
    }

    public String build() {
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        query.append(this.tableName).append(" (");
        List<String> primaryKeys = new ArrayList<>();
        for (Column column : this.columns) {
            query.append(column.name()).append(CommandDispatcher.ARGUMENT_SEPARATOR).append(column.type());
            if (column.isPrimary() == PrimaryColumn.TRUE) {
                primaryKeys.add(column.name());
            }
            query.append(", ");
        }
        query.setLength(query.length() - 2);
        if (!primaryKeys.isEmpty()) {
            query.append(", PRIMARY KEY(");
            for (String pk : primaryKeys) {
                query.append(pk).append(", ");
            }
            query.setLength(query.length() - 2);
            query.append(")");
        }
        query.append(")");
        return query.toString();
    }

    /* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/sql/query/SQLTableBuilder$Column.class */
    public static final class Column {
        private final String name;
        private final String type;
        private final PrimaryColumn isPrimary;

        public Column(String name, String type, PrimaryColumn isPrimary) {
            this.name = name;
            this.type = type;
            this.isPrimary = isPrimary;
        }

        @Override
        public final String toString() {
            return "Column[name=" + this.name + ", type=" + this.type + ", isPrimary=" + this.isPrimary + "]";
        }

        @Override
        public final int hashCode() {
            return java.util.Objects.hash(this.name, this.type, this.isPrimary);
        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Column)) return false;
            Column that = (Column) o;
            return java.util.Objects.equals(this.name, that.name) && java.util.Objects.equals(this.type, that.type) && java.util.Objects.equals(this.isPrimary, that.isPrimary);
        }

        public String name() {
            return this.name;
        }

        public String type() {
            return this.type;
        }

        public PrimaryColumn isPrimary() {
            return this.isPrimary;
        }
    }
}
