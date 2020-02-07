package com.dev.cinema;

import com.dev.cinema.lib.Injector;
import com.dev.cinema.model.CinemaHall;
import com.dev.cinema.model.Movie;
import com.dev.cinema.model.MovieSession;
import com.dev.cinema.model.ShoppingCart;
import com.dev.cinema.model.User;
import com.dev.cinema.service.AuthenticationService;
import com.dev.cinema.service.CinemaHallService;
import com.dev.cinema.service.MovieService;
import com.dev.cinema.service.MovieSessionService;
import com.dev.cinema.service.ShoppingCartService;
import java.time.LocalDateTime;
import javax.naming.AuthenticationException;

public class Main {
    private static Injector injector = Injector.getInstance("com.dev.cinema");

    public static void main(String[] args) {
        MovieService movieService = (MovieService) injector
                .getInstance(MovieService.class);
        Movie movie = new Movie();
        movie.setTitle("Fast and Furious");
        movie.setDescription("Chase");
        movieService.add(movie);

        CinemaHallService cinemaHallService = (CinemaHallService) injector
                .getInstance(CinemaHallService.class);
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setCapacity(100);
        cinemaHall.setDescription("Blue");
        cinemaHall = cinemaHallService.add(cinemaHall);

        MovieSession movieSession = new MovieSession();
        LocalDateTime showTime = LocalDateTime.of(2020, 2, 5, 20, 00);
        movieSession.setCinemaHall(cinemaHall);
        movieSession.setMovie(movie);
        movieSession.setShowTime(showTime);
        MovieSessionService movieSessionService = (MovieSessionService) injector
                .getInstance(MovieSessionService.class);
        movieSessionService.add(movieSession);
        System.out.println(cinemaHall);
        movieSessionService.findAvailableSessions(movie.getId(), showTime.toLocalDate())
                .forEach(System.out::println);

        AuthenticationService authenticationService = (AuthenticationService) injector
                .getInstance(AuthenticationService.class);
        User newRegistratedUser = authenticationService.register("email", "1");
        System.out.println(newRegistratedUser);
        User userAfterLogin = null;
        try {
            userAfterLogin = authenticationService.login("email", "1");
        } catch (AuthenticationException e) {
            System.out.println("Incorrect email or password");
        }
        System.out.println(userAfterLogin);
        ShoppingCartService shoppingCartService = (ShoppingCartService) injector
                .getInstance(ShoppingCartService.class);
        shoppingCartService.addSession(movieSession, newRegistratedUser);
        ShoppingCart shoppingCart = shoppingCartService.getByUser(newRegistratedUser);
        System.out.println(shoppingCart);
    }
}
