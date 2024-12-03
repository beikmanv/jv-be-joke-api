package com.northcoders.jokemanagerapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.northcoders.jokemanagerapi.model.Joke;
import com.northcoders.jokemanagerapi.service.JokeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class JokeControllerTests {

    private static final Logger logger = LoggerFactory.getLogger(JokeControllerTests.class);

    @Mock
    private JokeService mockJokeService;

    @InjectMocks
    private JokeController jokeController;

    @Autowired
    private MockMvc mockMvcController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(jokeController).build();
        objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("POST /jokes - Success")
    void postJoke_Success() throws Exception {
        // Set up test data
        Joke newJoke = new Joke(null, "setupLine1", "punchLine1", Joke.JokeCategories.PUN, true, null, null);
        Joke createdJoke = new Joke(1L, "setupLine1", "punchLine1", Joke.JokeCategories.PUN, true, Instant.now(), Instant.now());

        // Mock service layer
        when(mockJokeService.addJokeItem(newJoke)).thenReturn(createdJoke);

        // Perform the request and validate
        this.mockMvcController.perform(MockMvcRequestBuilders.post("/api/v1/jokes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newJoke)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdJoke.getId()))
                .andExpect(jsonPath("$.setupLine").value(createdJoke.getSetupLine()))
                .andExpect(jsonPath("$.punchLine").value(createdJoke.getPunchLine()))
                .andExpect(jsonPath("$.category").value(createdJoke.getCategory().toString()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.modifiedAt").isNotEmpty());
    }

//    @Test
//    @DisplayName("POST /jokes - Invalid Payload")
//    void postJoke_InvalidPayload() throws Exception {
//        // Set up invalid test data (missing setupLine)
//        Joke invalidJoke = new Joke(null, "", "punchLine1", Joke.JokeCategories.PUN, true, null, null);
//
//        // Perform the request and validate
//        this.mockMvcController.perform(MockMvcRequestBuilders.post("/api/v1/jokes")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidJoke)))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }

    @Test
    @DisplayName("POST /jokes - Existing ID")
    void postJoke_ExistingId() throws Exception {
        // Set up test data with an existing ID
        Joke newJoke = new Joke(1L, "setupLine1", "punchLine1", Joke.JokeCategories.PUN, true, null, null);

        // Mock service layer
        when(mockJokeService.addJokeItem(newJoke)).thenThrow(new IllegalArgumentException("Joke with ID already exists"));

        // Perform the request and validate
        this.mockMvcController.perform(MockMvcRequestBuilders.post("/api/v1/jokes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newJoke)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("GET /jokes")
    void getJokes() throws Exception {
        try {
            // Set up test data
            Instant createdAt = Instant.parse("2021-02-09T11:19:42.12Z");
            Instant modifiedAt = Instant.parse("2021-02-09T15:20:42.12Z");

            List<Joke> jokes = new ArrayList<>();
            jokes.add(new Joke(1L, "setupLine1", "punchLine1", Joke.JokeCategories.PUN, true, createdAt, modifiedAt));
            jokes.add(new Joke(2L, "setupLine2", "punchLine2", Joke.JokeCategories.PROGRAMMING, true, createdAt, modifiedAt));

            // Log serialized JSON for debugging
            logger.debug("Jokes list: {}", jokes);
            logger.debug("Serialized JSON: {}", objectMapper.writeValueAsString(jokes));

            // Mock service layer
            when(mockJokeService.getAllJokes()).thenReturn(jokes);

            // Perform the request and validate
            this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/jokes")
                            .contentType("application/json"))
                    .andDo(print())
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdAt").value("2021-02-09T11:19:42.120Z"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].createdAt").value("2021-02-09T11:19:42.120Z"));

        } catch (Exception e) {
            logger.error("Error during test execution", e);
            throw e;
        }
    }
}
