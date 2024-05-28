package com.northcoders.jokemanagerapi.service;

import com.northcoders.jokemanagerapi.repository.JokeItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
class JokeServiceTest {

    @Mock
    private JokeItemRepository mockJokeItemRepository;

    @InjectMocks
    private JokeServiceImpl jokesServiceImpl;

    @Test
    @DisplayName("getJokeItems responds with a list of all jokes")
    void testGetAllJokes() {
    }
}