package com.demo.precapstone.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String imageUrl;
    @Setter
    @Column(length = 1000)
    private String prompt;
    @Setter
    private LocalDateTime genAt;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Image(String imageUrl, String prompt, LocalDateTime genAt, User user) {
        this.imageUrl = imageUrl;
        this.prompt = prompt;
        this.genAt = genAt;
        this.user = user;
    }
}
