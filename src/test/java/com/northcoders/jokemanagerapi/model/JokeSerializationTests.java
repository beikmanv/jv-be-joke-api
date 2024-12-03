package com.northcoders.jokemanagerapi.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northcoders.jokemanagerapi.config.ObjectMapperConfig;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JokeSerializationTests {

    @Test
    void testJokeSerialization() throws Exception {
        ObjectMapper mapper = ObjectMapperConfig.createObjectMapper();

        Instant createdAt = Instant.parse("2021-02-09T11:19:42.120Z");
        Instant modifiedAt = Instant.parse("2021-02-09T15:20:42.120Z");

        Joke joke = new Joke(1L, "setupLine1", "punchLine1", Joke.JokeCategories.PUN, true, createdAt, modifiedAt);

        String json = mapper.writeValueAsString(joke);
        System.out.println(json); // Print the JSON for manual inspection
        // Check specific fields in the JSON output
        assert(json.contains("\"createdAt\":\"2021-02-09T11:19:42.120Z\""));
        assert(json.contains("\"modifiedAt\":\"2021-02-09T15:20:42.120Z\""));
    }
}
