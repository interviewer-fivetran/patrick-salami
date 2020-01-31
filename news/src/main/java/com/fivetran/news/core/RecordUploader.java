package com.fivetran.news.core;

public abstract class RecordUploader {

    public abstract void submit(Record record);

    public abstract void saveState(Object o);
}
