package com.northcoders.jokemanagerapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northcoders.jokemanagerapi.model.Joke;
import com.northcoders.jokemanagerapi.service.JokeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@AutoConfigureMockMvc
@SpringBootTest
class JokeControllerTests {

    @Mock
    private JokeServiceImpl mockJokeServiceImpl;

    @InjectMocks
    private JokeController jokeController;

    @Autowired
    private MockMvc mockMvcController;

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(jokeController).build();
        mapper = new ObjectMapper();
    }


    @Test
    @DisplayName("GET /jokes")
    void getJokes() throws Exception {
        Instant createdAt = Instant.parse("2021-02-09T11:19:42.12Z");
        Instant modifiedAt = Instant.parse("2021-02-09T15:20:42.12Z");

        List<Joke> jokes = new ArrayList<Joke>();
        jokes.add( new Joke(1L, "setupLine1", "punchLine1", Joke.JokeCategories.PUN, true,  createdAt, modifiedAt));
        jokes.add( new Joke(2L, "setupLine2", "punchLine2", Joke.JokeCategories.PROGRAMMING, true,  createdAt, modifiedAt));


        // when we call mockService's method ??? we want it to return list of jokes
        when(mockJokeServiceImpl.getAllJokes()).thenReturn(jokes);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/v1/jokes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].setupLine1").value("setupLine1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].punchLine1").value("punchLine1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].setupLine2").value("setupLine2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].punchLine2").value("punchLine2"));
                // TODO: Add checks for remaining attributes of Joke Entity
    }

    @Test
    @DisplayName("POST /jokes using valid payload with new joke Id")
    void testPostJoke_WithNewJokeId() throws Exception {
        Instant createdAt = Instant.parse("2021-02-09T11:19:42.12Z");
        Instant modifiedAt = Instant.parse("2021-02-09T15:20:42.12Z");
        Joke joke = new Joke(1L, "setupLine1", "punchLine1", Joke.JokeCategories.PUN, true,  createdAt, modifiedAt));

        // Don't care when is being passed into mocked service (addJokeItem) so long as return above joke
        when(mockJokeServiceImpl.addJokeItem(any(Joke.class))).thenReturn(joke);

        // Has client been able to get json object back, with status code created
        this.mockMvcController.perform(
                MockMvcRequestBuilders.post("/api/vi/jokes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(joke)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Has controller layer tried to talk to service layer to insert book once?
        verify(mockJokeServiceImpl, times(1)).addJokeItem(any(Joke.class));



    }



}