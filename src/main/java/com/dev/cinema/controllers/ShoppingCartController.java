package com.dev.cinema.controllers;

import com.dev.cinema.dto.MovieSessionRequestDto;
import com.dev.cinema.dto.ShoppingCartResponseDto;
import com.dev.cinema.dto.TicketResponseDto;
import com.dev.cinema.model.MovieSession;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.Ticket;
import com.dev.cinema.model.User;
import com.dev.cinema.service.CinemaHallService;
import com.dev.cinema.service.MovieService;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.UserService;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shoppingcart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final MovieService movieService;
    private final CinemaHallService cinemaHallService;

    public ShoppingCartController(ShoppingCartService shoppingCartService,
                                  UserService userService, MovieService movieService,
                                  CinemaHallService cinemaHallService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.movieService = movieService;
        this.cinemaHallService = cinemaHallService;
    }

    @PostMapping("/addmoviesession")
    public String addMovieSession(@Valid @RequestBody MovieSessionRequestDto movieSessionRequestDto,
                                Principal principal) {
        MovieSession movieSession = new MovieSession();
        movieSession.setMovie(movieService.getById(movieSessionRequestDto.getMovieId()));
        movieSession.setCinemaHall(cinemaHallService.getById(movieSessionRequestDto
                .getCinemaHallId()));
        movieSession.setShowTime(LocalDateTime.parse(movieSessionRequestDto.getShowTime()));
        User user = userService.findByEmail(principal.getName());
        shoppingCartService.addSession(movieSession, user);
        return "Success";
    }

    @GetMapping("/getbyuserid")
    public ShoppingCartResponseDto getByUserId(Principal principal) {
        return transformToShoppingCartResponseDto(shoppingCartService
                .getByUser(userService.findByEmail(principal.getName())));
    }

    private TicketResponseDto transformToTicketDto(Ticket ticket) {
        TicketResponseDto ticketResponseDto = new TicketResponseDto();
        ticketResponseDto.setUserEmail(ticket.getUser().getEmail());
        MovieSession movieSession = ticket.getMovieSession();
        ticketResponseDto.setCinemaHallId(movieSession.getCinemaHall().getId());
        ticketResponseDto.setMovieSessionId(movieSession.getId());
        ticketResponseDto.setMovieTitle(movieSession.getMovie().getTitle());
        ticketResponseDto.setShowTime(movieSession.getShowTime().toString());
        return ticketResponseDto;
    }

    private ShoppingCartResponseDto transformToShoppingCartResponseDto(ShoppingCart shoppingCart) {
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        shoppingCartResponseDto.setUserEmail(shoppingCart.getUser().getEmail());
        shoppingCartResponseDto.setTickets(shoppingCart.getTickets()
                .stream()
                .map(this::transformToTicketDto)
                .collect(Collectors.toList()));
        return shoppingCartResponseDto;
    }
}
