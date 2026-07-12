package com.gamezone.controller;

import com.gamezone.model.Compra;
import com.gamezone.model.Juego;
import com.gamezone.model.Usuario;
import com.gamezone.repository.JuegoRepository;
import com.gamezone.service.CompraService;
import com.gamezone.service.UsuarioService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class CompraController {

    private final JuegoRepository juegoRepository;
    private final CompraService compraService;
    private final UsuarioService usuarioService;

    public CompraController(
            JuegoRepository juegoRepository,
            CompraService compraService,
            UsuarioService usuarioService) {

        this.juegoRepository = juegoRepository;
        this.compraService = compraService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/comprar")
    public String comprar(
            @RequestParam Long idJuego,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Usuario usuario =
                usuarioService.buscarPorCorreo(authentication.getName());

        Juego juego =
                juegoRepository.findById(idJuego).orElse(null);

        if (usuario == null || juego == null) {
            return "redirect:/";
        }

        boolean yaComprado =
                compraService.usuarioYaComproJuego(
                        usuario.getCorreo(),
                        juego.getNombre()
                );

        if (yaComprado) {
            return "redirect:/biblioteca?yaComprado";
        }

        Compra compra = new Compra();

        compra.setUsuario(usuario.getCorreo());
        compra.setJuego(juego.getNombre());
        compra.setPrecio(juego.getPrecioFinal());
        compra.setFecha(LocalDateTime.now());
        compra.setImagen(juego.getImagen());

        compra.setMetodoPago("Compra directa simulada");
        compra.setEstado("APROBADA");
        compra.setCodigoOperacion(
                compraService.generarCodigoOperacion()
        );
        compra.setTitularPago(usuario.getNombre());
        compra.setUltimosDigitosTarjeta("0000");

        Compra compraGuardada = compraService.guardar(compra);

        return "redirect:/comprobante/" + compraGuardada.getId();
    }

    @GetMapping("/biblioteca")
    public String biblioteca(
            Authentication authentication,
            Model model) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Usuario usuario =
                usuarioService.buscarPorCorreo(authentication.getName());

        model.addAttribute("usuario", usuario);

        model.addAttribute(
                "compras",
                compraService.comprasUsuario(usuario.getCorreo())
        );

        return "biblioteca";
    }
}