package com.fivetran.news;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.*;

import static com.fivetran.news.DateUtil.stringToDate;

/**
 * DO NOT MODIFY ANYTHING IN THIS CLASS.
 */
public class RealNewspaperApi implements NewspaperApi {
    @Override
    public Optional<NewspaperResponse> getStories(LocalDate date, int offset) {
        String endpoint = "https://us-central1-interview-222421.cloudfunctions.net/stories";
        Map<String, String> queryParams = new HashMap<>();

        queryParams.put("date", com.fivetran.news.DateUtil.dateToString(date));
        queryParams.put("offset", String.valueOf(offset));

        String responseString = fetchResponse(endpoint, queryParams);
        JsonObject responseObject = new JsonParser().parse(responseString).getAsJsonObject();

        if (responseObject.keySet().isEmpty())
            return Optional.empty();

        List<NewspaperStory> stories = new ArrayList<>();
        for (JsonElement element: responseObject.getAsJsonArray("stories")) {
            JsonObject storyObject = element.getAsJsonObject();

            String headline = storyObject.getAsJsonPrimitive("name").getAsString();
            LocalDate publishedOn = stringToDate(storyObject.getAsJsonPrimitive(Schema.PUBLISH_DATE_COLUMN).getAsString());
            Map<String, Integer> counts = jsonToMap(storyObject.getAsJsonObject("counts"), String.class, Integer.class);
            stories.add(new NewspaperStory(headline, publishedOn, counts));
        }

        int total = responseObject.getAsJsonPrimitive("total").getAsInt();
        int count = responseObject.getAsJsonPrimitive("count").getAsInt();
        int responseOffset = responseObject.getAsJsonPrimitive("offset").getAsInt();
        return Optional.of(new NewspaperResponse(stories, count, responseOffset, total));
    }

    @Override
    public LocalDate earliestStoryPublishDate() {
        String endpoint = "https://us-central1-interview-222421.cloudfunctions.net/earliest";
        String responseString = fetchResponse(endpoint, new HashMap<>());
        JsonObject responseObject = new JsonParser().parse(responseString).getAsJsonObject();
        return stringToDate(responseObject.getAsJsonPrimitive("earliest").getAsString());
    }

    private static <K, V> Map<K, V> jsonToMap(JsonObject jsonObject, Class<K> keyType, Class<V> valueType) {
        return new Gson().fromJson(jsonObject, TypeToken.getParameterized(Map.class, keyType, valueType).getType());
    }

    private static String fetchResponse(String url, Map<String, String> queryParams) {
        // Used by underlying API to simulate real-world data (unrelated to problem)
        queryParams.put("requestDate", com.fivetran.news.DateUtil.dateToString(LocalDate.now()));

        String fullUrl = url + queryParams(queryParams);
        try {
            URLConnection connection = new URL(fullUrl).openConnection();

            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;

                while ((line = in.readLine()) != null)
                    responseBuilder.append(line);
            }
            return responseBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error while fetching response", e);
        }
    }

    private static String queryParams(Map<String, String> queryParams) {
        if (queryParams.isEmpty()) return "";
        StringBuilder urlBuilder = new StringBuilder("?");
        for (Map.Entry<String, String> entry : queryParams.entrySet())
            urlBuilder
                    .append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        String params = urlBuilder.toString();
        return params.substring(0, params.length() - 1);
    }
}
