package com.gamezone.service;

import com.gamezone.model.Review;
import com.gamezone.repository.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public void guardar(Review review) {
        reviewRepository.save(review);
    }

    public List<Review> listarPorJuego(String juego) {
        return reviewRepository.findByJuegoOrderByFechaDesc(juego);
    }

    public int cantidadReviews(String juego) {
        return listarPorJuego(juego).size();
    }

    public double promedioPuntuacion(String juego) {
        List<Review> reviews = listarPorJuego(juego);

        if (reviews.isEmpty()) {
            return 0.0;
        }

        return reviews.stream()
                .mapToInt(Review::getPuntuacion)
                .average()
                .orElse(0.0);
    }
}