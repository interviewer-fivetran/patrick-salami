package com.fivetran.news;

import com.fivetran.news.core.Record;
import com.fivetran.news.core.RecordUploader;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.fivetran.news.DateUtil.dateToString;
import static org.junit.Assert.*;

public class Spec {
    @Test
    public void main() {
        NewspaperState state = new NewspaperState();
        NewspaperUpdater updater = new NewspaperUpdater();
        RealNewspaperApi api = new RealNewspaperApi();

        List<Record> records = new ArrayList<>();
        List<NewspaperState> states = new ArrayList<>();
        RecordUploader uploader =
                new RecordUploader() {
                    @Override
                    public void submit(Record record) {
                        records.add(record);
                    }

                    @Override
                    public void saveState(Object o) {
                        states.add(NewspaperState.copy((NewspaperState) o));
                    }
                };
        updater.update(uploader, api, state);

        LocalDate today = LocalDate.now();

        testStories(findByTable(records, Schema.STORIES_TABLE_NAME), today);
        testStoryCounts(findByTable(records, Schema.STORY_COUNTS_TABLE_NAME), today);
        testState(states, today);
    }

    private void testStories(List<Record> stories, LocalDate today) {
        assertEquals(6, stories.size());

        for (Record story : stories) {
            assertTrue(story.values.containsKey(Schema.HEADLINE_COLUMN));
            assertTrue(story.values.containsKey(Schema.PUBLISH_DATE_COLUMN));
        }

        List<Record> sortedStories = sortByStringValue(stories, Schema.PUBLISH_DATE_COLUMN, true);

        testStory(sortedStories.get(0), "A", dateToString(today));
        testStory(sortedStories.get(1), "B", dateToString(today));

        LocalDate yesterday = today.minusDays(1);
        testStory(sortedStories.get(2), "C", dateToString(yesterday));
        testStory(sortedStories.get(3), "D", dateToString(yesterday));

        LocalDate dayBefore = today.minusDays(2);
        testStory(sortedStories.get(4), "E", dateToString(dayBefore));
        testStory(sortedStories.get(5), "F", dateToString(dayBefore));
    }

    private void testStory(Record story, String expectedHeadline, String expectedPublishDate) {
        assertEquals("Missing headline '" + expectedHeadline + "'", expectedHeadline, story.getString(Schema.HEADLINE_COLUMN));
        assertEquals("Incorrect publish date for headline'" + expectedHeadline + "'", expectedPublishDate, story.getString(Schema.PUBLISH_DATE_COLUMN));
    }

    private void testStoryCounts(List<Record> storyCounts, LocalDate today) {
        assertEquals(12, storyCounts.size());

        for (Record storyCount : storyCounts) {
            assertTrue(storyCount.values.containsKey(Schema.HEADLINE_COLUMN));
            assertTrue(storyCount.values.containsKey(Schema.COUNT_DATE_COLUMN));
            assertTrue(storyCount.values.containsKey(Schema.COUNTS_COLUMN));
        }

        List<Record> todayCounts = findByStringValue(storyCounts, Schema.COUNT_DATE_COLUMN, dateToString(today));
        List<Record> todaySorted = sortByStringValue(todayCounts, Schema.HEADLINE_COLUMN, false);
        assertEquals(6, todaySorted.size());

        testStoryCount(todaySorted.get(0), "A", 10, today);
        testStoryCount(todaySorted.get(1), "B", 10, today);
        testStoryCount(todaySorted.get(2), "C", 10, today);
        testStoryCount(todaySorted.get(3), "D", 10, today);
        testStoryCount(todaySorted.get(4), "E", 10, today);
        testStoryCount(todaySorted.get(5), "F", 10, today);

        LocalDate yesterday = today.minusDays(1);
        List<Record> yesterdayCounts = findByStringValue(storyCounts, Schema.COUNT_DATE_COLUMN, dateToString(yesterday));
        List<Record> yesterdaySorted = sortByStringValue(yesterdayCounts, Schema.HEADLINE_COLUMN, false);
        assertEquals(4, yesterdaySorted.size());

        testStoryCount(yesterdaySorted.get(0), "C", 10, yesterday);
        testStoryCount(yesterdaySorted.get(1), "D", 10, yesterday);
        testStoryCount(yesterdaySorted.get(2), "E", 10, yesterday);
        testStoryCount(yesterdaySorted.get(3), "F", 10, yesterday);

        LocalDate dayBefore = today.minusDays(2);
        List<Record> dayBeforeCounts = findByStringValue(storyCounts, Schema.COUNT_DATE_COLUMN, dateToString(dayBefore));
        List<Record> dayBeforeSorted = sortByStringValue(dayBeforeCounts, Schema.HEADLINE_COLUMN, false);
        assertEquals(2, dayBeforeSorted.size());

        testStoryCount(dayBeforeSorted.get(0), "E", 10, dayBefore);
        testStoryCount(dayBeforeSorted.get(1), "F", 10, dayBefore);
    }

    private void testStoryCount(Record storyCount, String expectedHeadline, int expectedCounts, LocalDate countDate) {
        assertEquals("Missing headline '" + expectedHeadline + "'", expectedHeadline, storyCount.getString(Schema.HEADLINE_COLUMN));
        assertEquals("Incorrect read count for headline '" + expectedHeadline + "' on date " + dateToString(countDate) + "", expectedCounts, (int) storyCount.getInt(Schema.COUNTS_COLUMN));
    }

    private void testState(List<NewspaperState> states, LocalDate today) {
        assertNotEquals("Missing state: are you saving it?", 0, states.size());

        NewspaperState lastState = states.get(states.size() - 1);
        String stateMessage = "Incorrect last state: Are you saving state at the correct time?";
        assertEquals(stateMessage, today, lastState.lastDate);
        assertEquals(stateMessage, 1, (int) lastState.lastOffset);
    }

    private static List<Record> findByTable(List<Record> records, String tableName) {
        return records.stream().filter(record -> record.table.name.equals(tableName)).collect(Collectors.toList());
    }

    private static List<Record> sortByStringValue(List<Record> records, String column, boolean reverse) {
        Comparator<Record> comp = Comparator.comparing(record -> record.getString(column));
        if (reverse) comp = comp.reversed();
        return records.stream()
                .sorted(comp)
                .collect(Collectors.toList());
    }

    private static List<Record> findByStringValue(List<Record> records, String column, String value) {
        return records.stream()
                .filter(record -> record.getString(column).equals(value))
                .collect(Collectors.toList());
    }
}
