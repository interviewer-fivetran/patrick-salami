package com.fivetran.news;

import com.fivetran.news.core.Record;
import com.fivetran.news.core.RecordUploader;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class NewspaperUpdater {
    /**
     * Main connector logic. Should sync up to today and save state for subsequent syncs.
     * @param uploader interfaces with the Core
     * @param api interfaces with the Source
     * @param state is saved for subsequent syncs
     */
    public void update(RecordUploader uploader, RealNewspaperApi api, NewspaperState state) {
        LocalDate dateCursor = state.lastDate != null ? state.lastDate : api.earliestStoryPublishDate();
        LocalDate now = LocalDate.now();

        while(now.compareTo(dateCursor) > -1) {

            LocalDate date = LocalDate.of(dateCursor.getYear(), dateCursor.getMonth(),
                    dateCursor.getDayOfMonth());
            NewspaperResponse response;
            int offset = state.lastOffset != null ? state.lastOffset : 0;
            try {

                response = proccessStoriesForDate(date, offset, uploader, api);
            } catch(NoStoriesException e) {
                continue;
            } finally {
                dateCursor = dateCursor.plusDays(1);
            }

            for(int currentOffset = offset + 1; currentOffset < response.total; currentOffset++) {
                proccessStoriesForDate(date, currentOffset, uploader, api);
            }
        }
    }

    private NewspaperResponse proccessStoriesForDate(LocalDate date, int offset, RecordUploader uploader, NewspaperApi api) throws NoStoriesException{
        Optional<NewspaperResponse> optionalResponse = api.getStories(date, offset);
        if(!optionalResponse.isPresent()) {
            // error handling
            System.out.println("[ERROR] no response");
            throw new NoStoriesException(date);
        }
        NewspaperResponse response = optionalResponse.get();
        if(response.count < 1) {
            System.out.printf("[INFO] no stories for specified date: %s\n", date.toString());
            throw new NoStoriesException(date);
        }

        for(NewspaperStory story : response.stories) {
            processStory(story, uploader);
        }

        NewspaperState currentState = new NewspaperState();
        currentState.lastDate = date;
        currentState.lastOffset = offset;
        uploader.saveState(currentState);

        return response;
    }

    private void processStory(NewspaperStory story, RecordUploader uploader) {
        // write story to uploader
        Record storyRecord = new Record.Builder(Schema.STORIES)
                .addString(Schema.HEADLINE_COLUMN, story.headline)
                .addString(Schema.PUBLISH_DATE_COLUMN, DateUtil.dateToString(story.publishedOn))
                .build();
        uploader.submit(storyRecord);

        for(Map.Entry<String, Integer> entry : story.readsPerDay.entrySet()) {
            Record countRecord = new Record.Builder(Schema.STORY_COUNTS)
                    .addString(Schema.HEADLINE_COLUMN, story.headline)
                    .addString(Schema.COUNT_DATE_COLUMN, entry.getKey())
                    .addInt(Schema.COUNTS_COLUMN, entry.getValue())
                    .build();
            uploader.submit(countRecord);
        }
    }
}
