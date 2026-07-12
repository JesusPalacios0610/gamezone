package com.gamezone.controller;

import com.gamezone.model.Compra;
import com.gamezone.model.Usuario;
import com.gamezone.service.ComprobantePdfService;
import com.gamezone.service.CompraService;
import com.gamezone.service.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;

import org.openpdf.text.DocumentException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class ComprobanteController {

    private final CompraService compraService;
    private final UsuarioService usuarioService;
    private final ComprobantePdfService comprobantePdfService;

    public ComprobanteController(
            CompraService compraService,
            UsuarioService usuarioService,
            ComprobantePdfService comprobantePdfService
    ) {

        this.compraService = compraService;
        this.usuarioService = usuarioService;
        this.comprobantePdfService = comprobantePdfService;
    }

    @GetMapping("/comprobante/{id}")
    public String verComprobante(
            @PathVariable Long id,
            Authentication authentication,
            Model model
    ) {

        if (authentication == null
                || !authentication.isAuthenticated()) {

            return "redirect:/login";
        }

        Usuario usuario = usuarioService.buscarPorCorreo(
                authentication.getName()
        );

        Compra compra = compraService.buscarCompraDelUsuario(
                id,
                usuario.getCorreo()
        ).orElse(null);

        if (compra == null) {
            return "redirect:/biblioteca";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("compra", compra);

        return "comprobante";
    }

    @GetMapping("/comprobante/{id}/pdf")
    public void descargarComprobantePdf(
            @PathVariable Long id,
            Authentication authentication,
            HttpServletResponse response
    ) throws IOException, DocumentException {

        if (authentication == null
                || !authentication.isAuthenticated()) {

            response.sendRedirect("/login");
            return;
        }

        Usuario usuario = usuarioService.buscarPorCorreo(
                authentication.getName()
        );

        Compra compra = compraService.buscarCompraDelUsuario(
                id,
                usuario.getCorreo()
        ).orElse(null);

        if (compra == null) {
            response.sendRedirect("/biblioteca");
            return;
        }

        String codigo = compra.getCodigoOperacion() != null
                ? compra.getCodigoOperacion()
                : String.valueOf(compra.getId());

        String nombreArchivo =
                "comprobante-gamezone-" + codigo + ".pdf";

        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=\"" + nombreArchivo + "\""
        );

        comprobantePdfService.generarPdf(
                compra,
                usuario,
                response.getOutputStream()
        );
    }
}