package com.fivetran.news.core;

import java.util.HashMap;
import java.util.Map;

public class Table {
    public Map<String, Column> columns;
    public String name;

    private Table(String name) {
        this.name = name;
    }

    public static class Column {
        public Table table;
        public String name;
        public ValueType type;
        public Boolean primaryKey;
        public Column foreignKey;

        Column(Table table, String name, ValueType type, Boolean primaryKey, Column foreignKey) {
            this.table = table;
            this.name = name;
            this.type = type;
            this.primaryKey = primaryKey;
            this.foreignKey = foreignKey;
        }
    }

    @Override
    public String toString() {
        return "Table{name=" + name + "}";
    }

    public static class Builder {
        private Map<String, Column> columns;
        private Table table;

        public Builder(String tableName) {
            this.table = new Table(tableName);
            this.columns = new HashMap<>();
        }

        public Builder addColumn(String name, ValueType type) {
            return this.addColumn(name, type, false);
        }

        public Builder addColumn(String name, ValueType type, boolean primaryKey) {
            this.addColumn(name, type, primaryKey, null);
            return this;
        }

        public Builder addColumn(String name, ValueType type, boolean primaryKey, Column foreignKey) {
            this.columns.put(name, new Column(table, name, type, primaryKey, foreignKey));
            return this;
        }

        public Table build() {
            table.columns = this.columns;
            return table;
        }
    }
}
