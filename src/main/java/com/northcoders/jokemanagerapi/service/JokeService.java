package com.northcoders.jokemanagerapi.service;

import com.northcoders.jokemanagerapi.model.Joke;

import java.util.List;

public interface JokeService {
    List<Joke> getAllJokes();
    Joke addJokeItem(Joke joke);
}
