package com.northcoders.jokemanagerapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.northcoders.jokemanagerapi.model.Joke;
import com.northcoders.jokemanagerapi.serialization.InstantSerializer;
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
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
        // Initialize MockMvc with the JokeController
        mockMvcController = MockMvcBuilders.standaloneSetup(jokeController).build();

        // Create and configure ObjectMapper
        objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);

        // Register custom Instant serializer
        SimpleModule module = new SimpleModule();
        module.addSerializer(Instant.class, new InstantSerializer());
        objectMapper.registerModule(module);

        // Disable timestamps for dates
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    @Test
    @DisplayName("POST /jokes - Success")
    void postJoke_Success() throws Exception {
        // Set up test data
        Instant currentTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Joke newJoke = Joke.builder()
                .setupLine("setupLine1")
                .punchLine("punchLine1")
                .category(Joke.JokeCategories.PUN)
                .isFunny(true)
                .build();

        Joke createdJoke = newJoke.toBuilder()
                .id(1L)
                .createdAt(currentTime)
                .modifiedAt(currentTime)
                .build();

        // Mock service layer
        when(mockJokeService.addJokeItem(any(Joke.class))).thenReturn(createdJoke);

        // Log the JSON payload for debugging
        String jokeJson = objectMapper.writeValueAsString(newJoke);
        logger.debug("Request JSON: {}", jokeJson);

        // Perform the request and validate
        this.mockMvcController.perform(MockMvcRequestBuilders.post("/api/v1/jokes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jokeJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdJoke.getId()))
                .andExpect(jsonPath("$.setupLine").value(createdJoke.getSetupLine()))
                .andExpect(jsonPath("$.punchLine").value(createdJoke.getPunchLine()))
                .andExpect(jsonPath("$.category").value(createdJoke.getCategory().toString()))
                .andExpect(jsonPath("$.createdAt").value(currentTime.truncatedTo(ChronoUnit.SECONDS).toString()))
                .andExpect(jsonPath("$.modifiedAt").value(currentTime.truncatedTo(ChronoUnit.SECONDS).toString()));
    }


    @Test
    @DisplayName("POST /jokes - Existing ID")
    void postJoke_ExistingId() throws Exception {
        // Set up test data with an existing ID
        Joke newJoke = Joke.builder()
                .id(1L)
                .setupLine("setupLine1")
                .punchLine("punchLine1")
                .category(Joke.JokeCategories.PUN)
                .isFunny(true)
                .build();

        // Mock service layer to throw exception
        when(mockJokeService.addJokeItem(any(Joke.class)))
                .thenThrow(new IllegalArgumentException("Joke with ID already exists"));

        // Perform the request and validate
        this.mockMvcController.perform(MockMvcRequestBuilders.post("/api/v1/jokes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newJoke)))
                .andDo(print())
                .andExpect(status().isConflict());  // Expect HTTP 409 Conflict
    }


    @Test
    public void testPostJoke2() throws Exception {
        // Create a new Joke object
        Joke joke = new Joke();
        joke.setSetupLine("Why do programmers prefer dark mode?");
        joke.setPunchLine("Because light attracts bugs.");
        joke.setCategory(Joke.JokeCategories.PROGRAMMING);
        joke.setIsFunny(true);  // Ensure 'isFunny' is set

        // Mock the behavior of the JokeService to return a joke with an id
        Joke mockedJoke = joke.toBuilder().id(1L).build(); // Set an ID for the returned joke
        when(mockJokeService.addJokeItem(any(Joke.class))).thenReturn(mockedJoke);

        // Perform the POST request and check the response
        mockMvcController.perform(MockMvcRequestBuilders.post("/api/v1/jokes")
                        .contentType("application/json")  // Specify that the request body is JSON
                        .content(objectMapper.writeValueAsString(joke)))  // Serialize the Joke object to JSON
                .andExpect(MockMvcResultMatchers.status().isCreated())  // Assert the status is 201 Created
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());  // Assert that 'id' is returned
    }

    @Test
    public void testPostJoke() throws Exception {
        // Create a new Joke object
        Joke joke = Joke.builder()
                .setupLine("Why do programmers prefer dark mode?")
                .punchLine("Because light attracts bugs.")
                .category(Joke.JokeCategories.PROGRAMMING)
                .isFunny(true)
                .build();

        // Mock the behavior of the JokeService to return a joke with an id
        Joke mockedJoke = joke.toBuilder().id(1L).build(); // Set an ID for the returned joke
        when(mockJokeService.addJokeItem(any(Joke.class))).thenReturn(mockedJoke);

        // Perform the POST request and check the response
        mockMvcController.perform(MockMvcRequestBuilders.post("/api/v1/jokes")
                        .contentType("application/json")  // Specify that the request body is JSON
                        .content(objectMapper.writeValueAsString(joke)))  // Serialize the Joke object to JSON
                .andExpect(MockMvcResultMatchers.status().isCreated())  // Assert the status is 201 Created
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())  // Assert that 'id' is returned
                .andExpect(MockMvcResultMatchers.jsonPath("$.isFunny").value(true));  // Assert the 'isFunny' field is true in the response
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

//    @Test
//    @DisplayName("POST /key jokes")
//    void addJoke() throws Exception {
//        Instant currentTime = Instant.now().truncatedTo(ChronoUnit.SECONDS); // Truncate to seconds
//        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC); // Use the UTC formatter
//
//        // Create a Joke object
//        Joke jokeToAdd = Joke.builder()
//                .id(1L)
//                .setupLine("Programming with an IDE is sometimes an uphill battle.")
//                .punchLine("It feels like it's me VS Code")
//                .category(Joke.JokeCategories.PUN)
//                .isFunny(true)
//                .createdAt(currentTime)
//                .modifiedAt(currentTime)
//                .build();
//
//        // Convert the Joke object to JSON
//        String jokeToAddJSON = objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(jokeToAdd);
//
//        // Mock the service method
//        when(mockJokeService.addJokeItem(any(Joke.class))).thenReturn(jokeToAdd);
//
//        // Perform the POST request and check the response
//        this.mockMvcController.perform(MockMvcRequestBuilders.post("/api/v1/jokes")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jokeToAddJSON))
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))  // Assert the id is present
//                .andExpect(MockMvcResultMatchers.jsonPath("$.setupLine").value("Programming with an IDE is sometimes an uphill battle."))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.punchLine").value("It feels like it's me VS Code"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value(String.valueOf(Joke.JokeCategories.PUN)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.isFunny").value(true));
////                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(formatter.format(currentTime)))  // Format without milliseconds
////                .andExpect(MockMvcResultMatchers.jsonPath("$.modifiedAt").value(formatter.format(currentTime)));  // Format without milliseconds
//    }
//


}
