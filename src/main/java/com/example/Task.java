package com.example;

import lombok.Data;

import javax.persistence.*;

/**
 * JPA entity
 */
@Data
@Entity
class Task {
    @Id
    @GeneratedValue
    private long id;
    @Column
    private String message;

    @ManyToOne
    private User assignee;

    @ManyToOne
    private User author;
}
