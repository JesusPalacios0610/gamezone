package com.gamezone.controller;

import com.gamezone.service.CompraService;
import com.gamezone.service.JuegoService;
import com.gamezone.service.UsuarioService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminDashboardController {

    private final CompraService compraService;
    private final UsuarioService usuarioService;
    private final JuegoService juegoService;

    public AdminDashboardController(
            CompraService compraService,
            UsuarioService usuarioService,
            JuegoService juegoService
    ) {
        this.compraService = compraService;
        this.usuarioService = usuarioService;
        this.juegoService = juegoService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {

        List<String> juegosLabels = new ArrayList<>();
        List<Long> juegosVentas = new ArrayList<>();

        String juegoMasVendido = "Sin ventas";
        long mayorCantidadVentas = 0;

        for (Object[] fila : compraService.ventasPorJuego()) {

            String nombreJuego = String.valueOf(fila[0]);

            long cantidadVentas =
                    ((Number) fila[1]).longValue();

            juegosLabels.add(nombreJuego);
            juegosVentas.add(cantidadVentas);

            if (cantidadVentas > mayorCantidadVentas) {
                mayorCantidadVentas = cantidadVentas;
                juegoMasVendido = nombreJuego;
            }
        }

        List<String> fechasLabels = new ArrayList<>();
        List<Double> ingresosData = new ArrayList<>();

        for (Object[] fila : compraService.ingresosPorFecha()) {

            fechasLabels.add(String.valueOf(fila[0]));

            double ingreso =
                    fila[1] != null
                            ? ((Number) fila[1]).doubleValue()
                            : 0.0;

            ingresosData.add(ingreso);
        }

        model.addAttribute(
                "totalVentas",
                compraService.totalVentas()
        );

        model.addAttribute(
                "totalIngresos",
                compraService.totalIngresos()
        );

        model.addAttribute(
                "totalUsuarios",
                usuarioService.totalUsuarios()
        );

        model.addAttribute(
                "totalJuegos",
                juegoService.totalJuegos()
        );

        model.addAttribute(
                "juegoMasVendido",
                juegoMasVendido
        );

        model.addAttribute(
                "juegosLabels",
                juegosLabels
        );

        model.addAttribute(
                "juegosVentas",
                juegosVentas
        );

        model.addAttribute(
                "fechasLabels",
                fechasLabels
        );

        model.addAttribute(
                "ingresosData",
                ingresosData
        );

        model.addAttribute(
                "ultimasCompras",
                compraService.obtenerUltimasCompras()
        );

        return "admin-dashboard";
    }

    @GetMapping("/admin/ventas")
    public String historialVentas(Model model) {

        model.addAttribute(
                "compras",
                compraService.listarTodasOrdenadas()
        );

        model.addAttribute(
                "totalVentas",
                compraService.totalVentas()
        );

        model.addAttribute(
                "totalIngresos",
                compraService.totalIngresos()
        );

        return "admin-ventas";
    }
}