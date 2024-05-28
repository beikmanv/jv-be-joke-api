package com.northcoders.jokemanagerapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Joke {

    public enum JokeCategories {
        PROGRAMMING,
        PUN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    Long id;

    @Column
    String setupLine;

    @Column
    String punchLine;

    @Column
    JokeCategories category;

    @Column
    boolean isFunny;

    @Column
    Instant createdAt;

    @Column
    Instant modifiedAt;

}
