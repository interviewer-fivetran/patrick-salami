package com.fivetran.news;

import java.time.LocalDate;
import java.util.Map;

public class NewspaperStory {
    public String headline;
    public LocalDate publishedOn;
    public Map<String, Integer> readsPerDay;

    public NewspaperStory(String headline, LocalDate publishedOn, Map<String, Integer> readsPerDay) {
        this.headline = headline;
        this.publishedOn = publishedOn;
        this.readsPerDay = readsPerDay;
    }

    @Override
    public String toString() {
        return "NewspaperStory{" +
                "headline='" + headline + '\'' +
                ", publishedOn=" + publishedOn +
                ", readsPerDay=" + readsPerDay +
                '}';
    }
}
