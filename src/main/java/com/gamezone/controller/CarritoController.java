package com.gamezone.controller;

import com.gamezone.model.Carrito;
import com.gamezone.model.Juego;
import com.gamezone.model.Usuario;

import com.gamezone.repository.JuegoRepository;

import com.gamezone.service.CarritoService;
import com.gamezone.service.CompraService;
import com.gamezone.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JuegoRepository juegoRepository;

    @Autowired
    private CompraService compraService;

    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(
            @RequestParam Long idJuego,
            Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioService.buscarPorCorreo(authentication.getName());
        Juego juego = juegoRepository.findById(idJuego).orElse(null);

        if (usuario == null || juego == null) {
            return "redirect:/";
        }

        boolean yaComprado = compraService.usuarioYaComproJuego(
                usuario.getCorreo(),
                juego.getNombre()
        );

        if (yaComprado) {
            return "redirect:/biblioteca?yaComprado";
        }

        carritoService.agregarJuego(usuario.getCorreo(), juego);

        return "redirect:/carrito";
    }

    @GetMapping("/carrito")
    public String verCarrito(
            Authentication authentication,
            Model model) {

        if (authentication == null) {
            return "redirect:/login";
        }

        Usuario usuario = usuarioService.buscarPorCorreo(authentication.getName());
        Carrito carrito = carritoService.obtenerCarrito(usuario.getCorreo());

        model.addAttribute("usuario", usuario);
        model.addAttribute("carrito", carrito);
        model.addAttribute("total", carritoService.calcularTotal(carrito));

        return "carrito";
    }

    @PostMapping("/carrito/eliminar/{id}")
    public String eliminarItem(@PathVariable Long id) {
        carritoService.eliminarItem(id);
        return "redirect:/carrito";
    }
}