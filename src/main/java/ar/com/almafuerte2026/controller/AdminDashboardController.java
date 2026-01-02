package ar.com.almafuerte2026.controller;

import ar.com.almafuerte2026.service.JugadorService;
import ar.com.almafuerte2026.service.JuegoService;
import ar.com.almafuerte2026.model.Jugador;
import ar.com.almafuerte2026.model.Posicion;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    private final JugadorService jugadorService;
    private final JuegoService juegoService;

    public AdminDashboardController(JugadorService jugadorService, JuegoService juegoService) {
        this.jugadorService = jugadorService;
        this.juegoService = juegoService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("jugadores", jugadorService.findAll());
        model.addAttribute("juegos", juegoService.findAll());
        model.addAttribute("jugador", new Jugador());
        model.addAttribute("posiciones", Posicion.values());
        return "admin/dashboard";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/jugadores")
    public String adminJugadores(Model model) {
        model.addAttribute("jugadores", jugadorService.findAll());
        model.addAttribute("jugador", new Jugador());
        model.addAttribute("posiciones", Posicion.values());
        return "admin/jugadores";
    }
}
