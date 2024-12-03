package com.northcoders.jokemanagerapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Setup line cannot be empty")
    @Column
    String setupLine;

    @NotBlank(message = "Punch line cannot be empty")
    @Column
    String punchLine;

    @NotNull(message = "Category cannot be null")
    @Enumerated(EnumType.STRING)
    @Column
    JokeCategories category;

    @Column
    boolean isFunny;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    @Column
    Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    @Column
    Instant modifiedAt;
}
