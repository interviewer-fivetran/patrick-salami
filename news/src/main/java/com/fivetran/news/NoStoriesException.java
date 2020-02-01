package com.fivetran.news;

import java.time.LocalDate;

public class NoStoriesException extends RuntimeException {
    public NoStoriesException(LocalDate date) {
        super("no stories for date " + date.toString());
    }
}
