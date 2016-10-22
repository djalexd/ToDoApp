package com.example;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
}
