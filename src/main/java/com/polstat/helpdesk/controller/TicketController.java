package com.polstat.helpdesk.controller;

import com.polstat.helpdesk.dto.TicketDto;
import com.polstat.helpdesk.model.Ticket;
import com.polstat.helpdesk.model.User;
import com.polstat.helpdesk.service.TicketService;
import com.polstat.helpdesk.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a new ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ticket created successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Ticket.class))
                    }),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - requires valid JWT token",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody TicketDto ticketDto) {
        User user = getAuthenticatedUser();
        Ticket ticket = ticketService.createTicket(ticketDto, user);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "Update ticket by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ticket updated successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Ticket.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Ticket not found",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - requires valid JWT token",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTicket(@PathVariable Long id, @RequestBody TicketDto ticketDto) {
        return ResponseEntity.ok(ticketService.updateTicket(id, ticketDto));
    }

    @Operation(summary = "Delete ticket by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ticket deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Ticket not found",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - requires valid JWT token",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.ok("Ticket deleted successfully!");
    }

    @Operation(summary = "Get ticket by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ticket retrieved successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Ticket.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Ticket not found",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - requires valid JWT token",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @Operation(summary = "Retrieve all tickets")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List of tickets retrieved successfully",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Ticket.class))
                    }),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - requires valid JWT token",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsernameWithRoles(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}