package com.gamezone.repository;

import com.gamezone.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    List<Review> findByJuegoOrderByFechaDesc(String juego);

}