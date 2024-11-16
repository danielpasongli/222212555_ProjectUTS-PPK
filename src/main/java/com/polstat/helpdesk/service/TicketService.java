package com.polstat.helpdesk.service;

import com.polstat.helpdesk.dto.TicketDto;
import com.polstat.helpdesk.model.Status;
import com.polstat.helpdesk.model.Ticket;
import com.polstat.helpdesk.model.User;
import com.polstat.helpdesk.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket createTicket(TicketDto ticketDto, User user) {
        Ticket ticket = new Ticket();
        ticket.setTitle(ticketDto.getTitle());
        ticket.setDescription(ticketDto.getDescription());
        ticket.setStatus(Status.OPEN);
        ticket.setUser(user);
        ticketRepository.save(ticket);
        return ticket;
    }

    public Ticket updateTicket(Long id, TicketDto ticketDto) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setTitle(ticketDto.getTitle());
        ticket.setDescription(ticketDto.getDescription());
        ticketRepository.save(ticket);
        return ticket;
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}