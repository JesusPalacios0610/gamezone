package com.gamezone.repository;

import com.gamezone.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    Usuario findByCorreo(String correo);

}