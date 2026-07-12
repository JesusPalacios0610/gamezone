package com.gamezone.controller;

import com.gamezone.model.Usuario;
import com.gamezone.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute Usuario usuario) {

        usuario.setRol("ROLE_USER");

        usuarioService.guardar(usuario);

        return "redirect:/login?registroExitoso";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }
}