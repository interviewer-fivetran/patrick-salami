package com.fivetran.news.core;

public class TaggedValue {

    public ValueType type;
    public Integer intValue;
    public String stringValue;

    public TaggedValue(String str) {
        type = ValueType.String;
        stringValue = str;
    }

    public TaggedValue(int i) {
        type = ValueType.Int;
        intValue = i;
    }

    @Override
    public String toString() {
        return "TaggedValue{" +
                "type=" + type +
                ", intValue=" + intValue +
                ", stringValue='" + stringValue + '\'' +
                '}';
    }
}
