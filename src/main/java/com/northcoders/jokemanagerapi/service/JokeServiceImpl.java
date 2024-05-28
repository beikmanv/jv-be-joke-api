package com.northcoders.jokemanagerapi.service;

import com.northcoders.jokemanagerapi.model.Joke;
import com.northcoders.jokemanagerapi.repository.JokeItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class JokeServiceImpl implements JokeService {

    @Autowired
    JokeItemRepository jokeItemRepository;

    @Override
    public List<Joke> getAllJokes() {
        List<Joke> jokes = new ArrayList<>();
        jokeItemRepository.findAll().forEach(jokes::add);
        return jokes;
    }

    @Override
    public Joke addJokeItem(Joke joke) {
        Instant timestamp = Instant.now();
        joke.setCreatedAt(timestamp);
        joke.setModifiedAt(timestamp);
       return jokeItemRepository.save(joke);
    }
}
