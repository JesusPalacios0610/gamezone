package com.gamezone.controller;

import com.gamezone.model.Usuario;
import com.gamezone.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarUsuarios(
            Authentication authentication,
            Model model) {

        if (authentication == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "usuarios",
                usuarioService.listar()
        );

        return "admin-usuarios";
    }

    @PostMapping("/rol/{id}")
    public String cambiarRol(
            @PathVariable Long id) {

        usuarioService.cambiarRol(id);

        return "redirect:/admin/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(
            @PathVariable Long id) {

        usuarioService.eliminar(id);

        return "redirect:/admin/usuarios";
    }
}