package com.gamezone.repository;

import com.gamezone.model.Juego;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JuegoRepository extends JpaRepository<Juego, Long> {

    List<Juego> findByNombreContainingIgnoreCaseOrCategoriaContainingIgnoreCase(
            String nombre,
            String categoria
    );

}