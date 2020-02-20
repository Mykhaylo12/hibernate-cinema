package com.dev.cinema.controllers;

import com.dev.cinema.dto.MovieSessionRequestDto;
import com.dev.cinema.dto.MovieSessionResponseDto;
import com.dev.cinema.model.MovieSession;
import com.dev.cinema.service.CinemaHallService;
import com.dev.cinema.service.MovieService;
import com.dev.cinema.service.MovieSessionService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/moviesessions")
public class MovieSessionController {
    private final MovieService movieService;
    private final MovieSessionService movieSessionService;
    private final CinemaHallService cinemaHallService;

    public MovieSessionController(MovieService movieService,
                                  MovieSessionService movieSessionService,
                                  CinemaHallService cinemaHallService) {
        this.movieService = movieService;
        this.movieSessionService = movieSessionService;
        this.cinemaHallService = cinemaHallService;
    }

    @PostMapping("/add")
    public void add(@RequestBody MovieSessionRequestDto movieSessionRequestDto) {
        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(movieService.getById(movieSessionRequestDto.getMovieId()));
        movieSession.setCinemaHall(cinemaHallService.getById(movieSessionRequestDto
                .getCinemaHallId()));
        movieSession.setShowTime(LocalDateTime.parse(movieSessionRequestDto.getShowTime()));
        movieSessionService.add(movieSession);
    }

    @GetMapping("/available")
    public List<MovieSessionResponseDto> findAvailableSessions(@RequestParam Long movieId,
                                                               @RequestParam @DateTimeFormat(iso
                                                                       = DateTimeFormat.ISO.DATE)
                                                                       LocalDate localDate) {
        return movieSessionService.findAvailableSessions(movieId, localDate)
                .stream()
                .map(this::transformToMovieSessionResponseDto).collect(Collectors.toList());
    }

    private MovieSessionResponseDto transformToMovieSessionResponseDto(MovieSession movieSession) {
        MovieSessionResponseDto movieSessionResponseDto = new MovieSessionResponseDto();
        movieSessionResponseDto.setCinemaHallId(movieSession.getCinemaHall().getId());
        movieSessionResponseDto.setMovieId(movieSession.getMovie().getId());
        movieSessionResponseDto.setShowTime(movieSession.getShowTime().toString());
        return movieSessionResponseDto;
    }
}
