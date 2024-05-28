package com.northcoders.jokemanagerapi.controller;

import com.northcoders.jokemanagerapi.model.Joke;
import com.northcoders.jokemanagerapi.service.JokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("api/v1/jokes")
public class JokeController {
     @Autowired
     private JokeService jokeService;

    @GetMapping
        public ResponseEntity<List<Joke>> getAllJokes() {
        return new ResponseEntity<>(jokeService.getAllJokes(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Joke> postJoke(@RequestBody Joke postedJoke) {
       Joke addedJoke = jokeService.addJokeItem(postedJoke);
        return new ResponseEntity<>(addedJoke, HttpStatus.CREATED);
    }


}
