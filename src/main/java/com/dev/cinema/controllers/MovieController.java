package com.dev.cinema.controllers;

import com.dev.cinema.dto.MovieRequestDto;
import com.dev.cinema.dto.MovieResponseDto;
import com.dev.cinema.model.Movie;
import com.dev.cinema.service.MovieService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/add")
    public MovieResponseDto add(@RequestBody MovieRequestDto movieRequestDto) {
        Movie movie = new Movie();
        movie.setTitle(movieRequestDto.getTitle());
        movie.setDescription(movieRequestDto.getDescription());
        movieService.add(movie);
        MovieResponseDto movieResponseDto = new MovieResponseDto();
        movieResponseDto.setDescription(movie.getDescription());
        movieResponseDto.setTitle(movie.getTitle());
        return movieResponseDto;
    }

    @GetMapping("/get")
    public List<MovieResponseDto> getAll() {
        return movieService.getAll()
                .stream()
                .map(this::transformToMovieResponseDto)
                .collect(Collectors.toList());
    }

    private MovieResponseDto transformToMovieResponseDto(Movie movie) {
        MovieResponseDto userResponseDto = new MovieResponseDto();
        userResponseDto.setTitle(movie.getTitle());
        userResponseDto.setDescription(movie.getDescription());
        return userResponseDto;
    }
}
