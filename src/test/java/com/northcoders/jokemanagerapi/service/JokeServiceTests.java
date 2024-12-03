package com.northcoders.jokemanagerapi.service;

import com.northcoders.jokemanagerapi.model.Joke;
import com.northcoders.jokemanagerapi.repository.JokeItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class JokeServiceTests {

    @Mock
    private JokeItemRepository jokeItemRepository;

    @InjectMocks
    private JokeServiceImpl jokeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Add Joke - Success")
    void addJoke_Success() {
        // Set up test data
        Joke newJoke = new Joke(null, "setupLine1", "punchLine1", Joke.JokeCategories.PUN, true, null, null);
        Joke createdJoke = new Joke(1L, "setupLine1", "punchLine1", Joke.JokeCategories.PUN, true, Instant.now(), Instant.now());

        // Mock repository layer
        when(jokeItemRepository.save(any(Joke.class))).thenReturn(createdJoke);

        // Call service layer
        Joke result = jokeService.addJokeItem(newJoke);

        // Validate result
        assertEquals(createdJoke.getId(), result.getId());
        assertEquals(createdJoke.getSetupLine(), result.getSetupLine());
        assertEquals(createdJoke.getPunchLine(), result.getPunchLine());
        assertEquals(createdJoke.getCategory(), result.getCategory());
    }

    @Test
    @DisplayName("Add Joke - Existing ID")
    void addJoke_ExistingId() {
        // Set up test data with an existing ID
        Joke newJoke = new Joke(1L, "setupLine1", "punchLine1", Joke.JokeCategories.PUN, true, null, null);

        // Mock repository layer to throw exception
        when(jokeItemRepository.save(any(Joke.class))).thenThrow(new IllegalArgumentException("Joke with ID already exists"));

        // Call service layer and validate exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            jokeService.addJokeItem(newJoke);
        });

        assertEquals("Joke with ID already exists", exception.getMessage());
    }
}
