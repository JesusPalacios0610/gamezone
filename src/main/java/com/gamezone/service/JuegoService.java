package com.gamezone.service;

import com.gamezone.model.Juego;
import com.gamezone.repository.JuegoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JuegoService {

    @Autowired
    private JuegoRepository repository;

    public List<Juego> listar() {
        return repository.findAll();
    }

    public Juego buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void guardar(Juego juego) {
        repository.save(juego);
    }

    public List<Juego> buscar(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return listar();
        }

        return repository.findByNombreContainingIgnoreCaseOrCategoriaContainingIgnoreCase(
                texto,
                texto
        );
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
    public Long totalJuegos() {
    return repository.count();
}
}