package com.gamezone.controller;

import com.gamezone.model.Carrito;
import com.gamezone.model.CarritoItem;
import com.gamezone.model.Compra;
import com.gamezone.model.Usuario;
import com.gamezone.service.CarritoService;
import com.gamezone.service.CompraService;
import com.gamezone.service.UsuarioService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class CheckoutController {

    private final CarritoService carritoService;
    private final CompraService compraService;
    private final UsuarioService usuarioService;

    public CheckoutController(
            CarritoService carritoService,
            CompraService compraService,
            UsuarioService usuarioService) {

        this.carritoService = carritoService;
        this.compraService = compraService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/checkout")
    public String mostrarCheckout(
            Authentication authentication,
            Model model) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Usuario usuario =
                usuarioService.buscarPorCorreo(authentication.getName());

        Carrito carrito =
                carritoService.obtenerCarrito(usuario.getCorreo());

        if (carrito.getItems().isEmpty()) {
            return "redirect:/carrito";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("carrito", carrito);
        model.addAttribute(
                "total",
                carritoService.calcularTotal(carrito)
        );

        return "checkout";
    }

    @PostMapping("/checkout/pagar")
    public String pagar(
            @RequestParam String titular,
            @RequestParam String numeroTarjeta,
            @RequestParam String vencimiento,
            @RequestParam String cvv,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Usuario usuario =
                usuarioService.buscarPorCorreo(authentication.getName());

        Carrito carrito =
                carritoService.obtenerCarrito(usuario.getCorreo());

        if (carrito.getItems().isEmpty()) {
            return "redirect:/carrito";
        }

        String tarjetaLimpia =
                numeroTarjeta.replaceAll("\\s+", "");

        if (titular == null
                || titular.isBlank()
                || !tarjetaLimpia.matches("\\d{12,16}")
                || vencimiento == null
                || !vencimiento.matches("\\d{2}/\\d{2}")
                || cvv == null
                || !cvv.matches("\\d{3,4}")) {

            return "redirect:/checkout?error";
        }

        Compra ultimaCompra = null;

        for (CarritoItem item : carrito.getItems()) {

            boolean yaComprado =
                    compraService.usuarioYaComproJuego(
                            usuario.getCorreo(),
                            item.getNombreJuego()
                    );

            if (!yaComprado) {

                Compra compra = new Compra();

                compra.setUsuario(usuario.getCorreo());
                compra.setJuego(item.getNombreJuego());
                compra.setPrecio(item.getSubtotal());
                compra.setFecha(LocalDateTime.now());
                compra.setImagen(item.getImagen());

                compra.setMetodoPago("Tarjeta simulada");
                compra.setEstado("APROBADA");
                compra.setCodigoOperacion(
                        compraService.generarCodigoOperacion()
                );

                compra.setTitularPago(titular.trim());

                compra.setUltimosDigitosTarjeta(
                        tarjetaLimpia.substring(
                                tarjetaLimpia.length() - 4
                        )
                );

                ultimaCompra = compraService.guardar(compra);
            }
        }

        carritoService.vaciarCarrito(carrito);

        if (ultimaCompra == null) {
            return "redirect:/biblioteca?yaComprado";
        }

        return "redirect:/comprobante/" + ultimaCompra.getId();
    }
}