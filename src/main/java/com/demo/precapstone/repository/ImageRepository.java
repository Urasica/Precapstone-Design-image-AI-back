package com.demo.precapstone.repository;

import com.demo.precapstone.dao.User;
import com.demo.precapstone.dao.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

    public interface ImageRepository extends JpaRepository<Image, Long> {
        List<Image> findByUserOrderByGenAtDesc(User user);
    }

