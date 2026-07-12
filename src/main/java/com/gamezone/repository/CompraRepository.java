package com.gamezone.repository;

import com.gamezone.model.Compra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Long> {

    List<Compra> findByUsuarioOrderByFechaDesc(String usuario);

    List<Compra> findAllByOrderByFechaDesc();

    List<Compra> findTop10ByOrderByFechaDesc();

    boolean existsByUsuarioAndJuego(String usuario, String juego);

    Optional<Compra> findByIdAndUsuario(Long id, String usuario);

    @Query("""
            SELECT c.juego, COUNT(c)
            FROM Compra c
            GROUP BY c.juego
            ORDER BY COUNT(c) DESC
            """)
    List<Object[]> contarVentasPorJuego();

    @Query("""
            SELECT FUNCTION('DATE', c.fecha), SUM(c.precio)
            FROM Compra c
            GROUP BY FUNCTION('DATE', c.fecha)
            ORDER BY FUNCTION('DATE', c.fecha)
            """)
    List<Object[]> ingresosPorFecha();

    @Query("SELECT SUM(c.precio) FROM Compra c")
    Double totalIngresos();
}