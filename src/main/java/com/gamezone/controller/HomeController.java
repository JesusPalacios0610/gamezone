package com.gamezone.controller;

import com.gamezone.model.Compra;
import com.gamezone.model.Juego;
import com.gamezone.model.Usuario;
import com.gamezone.service.CompraService;
import com.gamezone.service.JuegoService;
import com.gamezone.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CompraService compraService;

    @GetMapping("/")
    public String inicio(
            @RequestParam(required = false) String buscar,
            Authentication authentication,
            Model model) {

        Usuario usuario = null;
        List<String> juegosComprados = List.of();

        if (authentication != null && authentication.isAuthenticated()) {
            usuario = usuarioService.buscarPorCorreo(authentication.getName());

            if (usuario != null) {
                juegosComprados = compraService.comprasUsuario(usuario.getCorreo())
                        .stream()
                        .map(Compra::getJuego)
                        .collect(Collectors.toList());
            }
        }

        List<Juego> juegos = juegoService.buscar(buscar);

        List<Juego> destacados = juegos.stream()
                .filter(j -> j.getDestacado() != null && j.getDestacado())
                .collect(Collectors.toList());

        model.addAttribute("usuario", usuario);
        model.addAttribute("juegos", juegos);
        model.addAttribute("destacados", destacados);
        model.addAttribute("juegosComprados", juegosComprados);
        model.addAttribute("buscar", buscar);

        return "index";
    }
}