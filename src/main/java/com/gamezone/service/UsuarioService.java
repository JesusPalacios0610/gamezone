package com.gamezone.service;

import com.gamezone.model.Usuario;
import com.gamezone.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void guardar(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        if (usuario.getRol() == null || usuario.getRol().isBlank()) {
            usuario.setRol("ROLE_USER");
        }

        usuarioRepository.save(usuario);
    }

    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public void cambiarRol(Long id) {
        Usuario usuario = buscarPorId(id);

        if (usuario == null) {
            return;
        }

        if ("ROLE_ADMIN".equals(usuario.getRol())) {
            usuario.setRol("ROLE_USER");
        } else {
            usuario.setRol("ROLE_ADMIN");
        }

        usuarioRepository.save(usuario);
    }
    public Long totalUsuarios() {
    return usuarioRepository.count();
}
}