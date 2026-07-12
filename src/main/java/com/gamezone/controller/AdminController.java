package com.gamezone.controller;

import com.gamezone.model.Juego;
import com.gamezone.service.JuegoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminController {

    @Autowired
    private JuegoService juegoService;

    @GetMapping("/admin/juegos")
    public String mostrarPanelAdmin(Model model) {

        model.addAttribute("juego", new Juego());
        model.addAttribute("juegos", juegoService.listar());
        model.addAttribute("modoEditar", false);

        return "admin-juegos";
    }

    @PostMapping("/admin/juegos")
    public String guardarJuego(@ModelAttribute Juego juego) {

        if (juego.getDescuento() == null) {
            juego.setDescuento(0.0);
        }

        if (juego.getDestacado() == null) {
            juego.setDestacado(false);
        }

        juegoService.guardar(juego);

        return "redirect:/admin/juegos";
    }

    @GetMapping("/admin/juegos/editar/{id}")
    public String editarJuego(
            @PathVariable Long id,
            Model model) {

        Juego juego = juegoService.buscarPorId(id);

        model.addAttribute("juego", juego);
        model.addAttribute("juegos", juegoService.listar());
        model.addAttribute("modoEditar", true);

        return "admin-juegos";
    }

    @PostMapping("/admin/juegos/eliminar/{id}")
    public String eliminarJuego(@PathVariable Long id) {

        juegoService.eliminar(id);

        return "redirect:/admin/juegos";
    }
}