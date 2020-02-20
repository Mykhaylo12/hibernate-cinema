package com.dev.cinema.dto;

import java.util.List;

public class ShoppingCartResponseDto {
    private List<TicketResponseDto> tickets;
    private String userEmail;

    public List<TicketResponseDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketResponseDto> tickets) {
        this.tickets = tickets;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
