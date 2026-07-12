package com.gamezone.controller;

import com.gamezone.model.Review;
import com.gamezone.model.Usuario;
import com.gamezone.service.ReviewService;
import com.gamezone.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/review/guardar")
    public String guardarReview(
            @RequestParam Long idJuego,
            @RequestParam String juego,
            @RequestParam Integer puntuacion,
            @RequestParam String comentario,
            Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioService.buscarPorCorreo(authentication.getName());

        Review review = new Review();
        review.setUsuario(usuario.getNombre());
        review.setJuego(juego);
        review.setPuntuacion(puntuacion);
        review.setComentario(comentario);
        review.setFecha(LocalDate.now());

        reviewService.guardar(review);

        return "redirect:/juego/" + idJuego;
    }
}