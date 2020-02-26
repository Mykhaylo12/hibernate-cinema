package com.dev.cinema.controllers;

import com.dev.cinema.dto.OrderResponseDto;
import com.dev.cinema.model.Order;
import com.dev.cinema.model.Ticket;
import com.dev.cinema.model.User;
import com.dev.cinema.service.OrderService;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.UserService;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    public OrderController(OrderService orderService, ShoppingCartService shoppingCartService,
                           UserService userService) {
        this.orderService = orderService;
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @PostMapping("/complete")
    public OrderResponseDto completeOrder(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        List<Ticket> list = shoppingCartService
                .getByUser(userService.getById(user.getId())).getTickets();
        Order order = orderService.completeOrder(list, user);
        orderResponseDto.setOrderId(order.getId());
        orderResponseDto.setUserId(order.getUser().getId());
        return orderResponseDto;
    }

    @GetMapping("/all")
    public List<OrderResponseDto> getOrderHistory(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return orderService.getOrderHistory(user)
                .stream()
                .map(this::transformToOrderResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/allorders")
    public List<OrderResponseDto> getAll() {
        return orderService.getAll()
                .stream()
                .map(this::transformToOrderResponseDto)
                .collect(Collectors.toList());
    }

    private OrderResponseDto transformToOrderResponseDto(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setUserId(order.getUser().getId());
        orderResponseDto.setOrderId(order.getId());
        return orderResponseDto;
    }
}
