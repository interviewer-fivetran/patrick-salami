package com.fivetran.news;

import java.util.List;

public class NewspaperResponse {
    public List<NewspaperStory> stories;
    public int count;
    public int offset;
    public int total;

    public NewspaperResponse(List<NewspaperStory> stories, int count, int offset, int total) {
        this.stories = stories;
        this.count = count;
        this.offset = offset;
        this.total = total;
    }

    @Override
    public String toString() {
        return "NewspaperResponse{" +
                "stories=" + stories +
                ", count=" + count +
                ", offset=" + offset +
                ", total=" + total +
                '}';
    }
}
