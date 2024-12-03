package com.northcoders.jokemanagerapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectMapperConfigTests {

    @Test
    void testObjectMapperConfiguration() throws Exception {
        ObjectMapper mapper = ObjectMapperConfig.createObjectMapper();
        Instant now = Instant.parse("2021-02-09T11:19:42.120Z");

        String json = mapper.writeValueAsString(now);
        assertEquals("\"2021-02-09T11:19:42.120Z\"", json);
    }
}
