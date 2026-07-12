package com.gamezone.service;

import com.gamezone.model.Compra;
import com.gamezone.repository.CompraRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompraService {

    private final CompraRepository compraRepository;

    public CompraService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    public Compra guardar(Compra compra) {
        return compraRepository.save(compra);
    }

    public List<Compra> comprasUsuario(String usuario) {
        return compraRepository.findByUsuarioOrderByFechaDesc(usuario);
    }

    public List<Compra> listarTodasOrdenadas() {
        return compraRepository.findAllByOrderByFechaDesc();
    }

    public List<Compra> obtenerUltimasCompras() {
        return compraRepository.findTop10ByOrderByFechaDesc();
    }

    public boolean usuarioYaComproJuego(String usuario, String juego) {
        return compraRepository.existsByUsuarioAndJuego(usuario, juego);
    }

    public Optional<Compra> buscarCompraDelUsuario(Long id, String usuario) {
        return compraRepository.findByIdAndUsuario(id, usuario);
    }

    public Optional<Compra> buscarPorId(Long id) {
        return compraRepository.findById(id);
    }

    public String generarCodigoOperacion() {
        String fecha = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String aleatorio = UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();

        return "GZ-" + fecha + "-" + aleatorio;
    }

    public List<Compra> listarTodas() {
        return compraRepository.findAll();
    }

    public Long totalVentas() {
        return compraRepository.count();
    }

    public Double totalIngresos() {
        Double total = compraRepository.totalIngresos();
        return total != null ? total : 0.0;
    }

    public List<Object[]> ventasPorJuego() {
        return compraRepository.contarVentasPorJuego();
    }

    public List<Object[]> ingresosPorFecha() {
        return compraRepository.ingresosPorFecha();
    }
}