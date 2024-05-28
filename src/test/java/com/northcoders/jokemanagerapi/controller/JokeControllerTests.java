package com.northcoders.jokemanagerapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.northcoders.jokemanagerapi.service.JokeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


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

    }
}