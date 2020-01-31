package com.fivetran.news;

import com.fivetran.news.core.Table;

public class Schema {
    // Table names
    static final String STORIES_TABLE_NAME = "Stories";
    static final String STORY_COUNTS_TABLE_NAME = "StoryCounts";

    // Column names
    static final String HEADLINE_COLUMN = "headline";
    static final String PUBLISH_DATE_COLUMN = "publishedOn";
    static final String COUNT_DATE_COLUMN = "countDate";
    static final String COUNTS_COLUMN = "counts";

    static final Table STORIES = new Table.Builder(STORIES_TABLE_NAME).build();

    static final Table STORY_COUNTS = new Table.Builder(STORY_COUNTS_TABLE_NAME).build();
}
