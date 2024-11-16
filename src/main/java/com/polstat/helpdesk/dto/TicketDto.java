package com.polstat.helpdesk.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class TicketDto {
    @NotBlank
    private String title;

    private String description;
}