package com.gamezone.repository;

import com.gamezone.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Carrito findByUsuario(String usuario);
}