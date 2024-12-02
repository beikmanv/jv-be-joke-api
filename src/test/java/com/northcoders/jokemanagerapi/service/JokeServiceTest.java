package com.northcoders.jokemanagerapi.service;

import com.northcoders.jokemanagerapi.model.Joke;
import com.northcoders.jokemanagerapi.repository.JokeItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;


@DataJpaTest
class JokeServiceTest {

    @Mock
    private JokeItemRepository mockJokeItemRepository;

    @InjectMocks
    private JokeServiceImpl jokesServiceImpl;

    @Test
    @DisplayName("getJokeItems responds with a list of all jokes")
    void testGetAllJokes() {
        Instant createdAt = Instant.parse("2021-02-09T11:19:42.12Z");
        Instant modifiedAt = Instant.parse("2021-02-09T15:20:42.12Z");

        List<Joke> jokes = new ArrayList<Joke>();
        jokes.add( new Joke(1L, "setupLine1", "punchLine1", Joke.JokeCategories.PUN, true,  createdAt, modifiedAt));
        jokes.add( new Joke(2L, "setupLine2", "punchLine2", Joke.JokeCategories.PROGRAMMING, true,  createdAt, modifiedAt));

        //when call mock Repo's function findAll, return list of jokes
        when(mockJokeItemRepository.findAll()).thenReturn(jokes);

        // service return list actualJokes
        List<Joke> actualJokes = jokesServiceImpl.getAllJokes();

        // Assert
        // assertEquals(actualJokes.size(), 2);
        assertThat(actualJokes).hasSize(2);
        assertThat(actualJokes).isEqualTo(jokes);

    }
}