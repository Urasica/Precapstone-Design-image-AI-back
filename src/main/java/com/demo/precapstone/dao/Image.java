package com.demo.precapstone.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;
    private String prompt;
    private LocalDateTime genAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
