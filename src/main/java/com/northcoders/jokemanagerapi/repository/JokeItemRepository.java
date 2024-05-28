package com.northcoders.jokemanagerapi.repository;

import com.northcoders.jokemanagerapi.model.Joke;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JokeItemRepository  extends CrudRepository<Joke, Long> {
}
