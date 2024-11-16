package com.polstat.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import jakarta.persistence.*;

import java.util.Set;
import java.util.HashSet;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.PERSIST)
    @JsonBackReference
    private Set<User> users = new HashSet<>();

}
