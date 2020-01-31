package com.fivetran.news;

import java.time.LocalDate;
import java.util.Optional;

public interface NewspaperApi {
    /**
     * Fetches newspaper story read count data
     * @param date story was published
     * @param offset story within result set
     * @return Optional containing response. If query is invalid or no data is found, Optional.empty().
     */
    Optional<NewspaperResponse> getStories(LocalDate date, int offset);

    LocalDate earliestStoryPublishDate();
}
