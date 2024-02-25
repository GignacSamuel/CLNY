package com.example.clny.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Inheritance
@Getter
@Setter
public abstract class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne
    private User user;

    private final Date notificationDate = new Date();

    private boolean read;

    public Notification() {
    }

    public Notification(User user) {
        this.user = user;
    }

}
