package com.fivetran.news.core;

import java.util.SortedMap;
import java.util.TreeMap;

public class Record {
    public Table table;
    public SortedMap<String, TaggedValue> values;

    private Record(Table table, SortedMap<String, TaggedValue> values) {
        this.table = table;
        this.values = values;
    }

    public String getString(String field) {
        TaggedValue value = values.getOrDefault(field, null);
        if (value == null) return null;
        return value.stringValue;
    }

    public Integer getInt(String field) {
        TaggedValue value = values.getOrDefault(field, null);
        if (value == null) return null;
        return value.intValue;
    }

    @Override
    public String toString() {
        return "Record{" +
                "table=" + table +
                ", values=" + values +
                '}';
    }

    public static class Builder {
        Table table;
        SortedMap<String, TaggedValue> values;

        public Builder(Table table) {
            this.table = table;
            this.values = new TreeMap<>();
        }

        public Builder addInt(String field, int i) {
            this.values.put(field, new TaggedValue(i));
            return this;
        }

        public Builder addString(String field, String s) {
            this.values.put(field, new TaggedValue(s));
            return this;
        }

        public Record build() {
            return new Record(this.table, this.values);
        }
    }
}
