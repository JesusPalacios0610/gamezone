package com.gamezone.controller;

import com.gamezone.model.Juego;
import com.gamezone.service.JuegoService;
import com.gamezone.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class JuegoController {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/juego/{id}")
    public String detalleJuego(
            @PathVariable Long id,
            Model model) {

        Juego juego = juegoService.buscarPorId(id);

        if (juego == null) {
            return "redirect:/";
        }

        model.addAttribute("juego", juego);
        model.addAttribute("reviews", reviewService.listarPorJuego(juego.getNombre()));
        model.addAttribute("cantidadReviews", reviewService.cantidadReviews(juego.getNombre()));
        model.addAttribute("promedioReviews", reviewService.promedioPuntuacion(juego.getNombre()));

        return "detalle-juego";
    }
}