package com.dev.cinema.dto;

import org.springframework.lang.NonNull;
import javax.validation.constraints.Positive;

public class CinemaHallRequestDto {
    @NonNull
    private int capacity;
    private String description;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
