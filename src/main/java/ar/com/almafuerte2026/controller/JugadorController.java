package ar.com.almafuerte2026.controller;

import ar.com.almafuerte2026.model.Jugador;
import ar.com.almafuerte2026.repository.JugadorRepository;
import ar.com.almafuerte2026.service.JugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class JugadorController {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private JugadorService jugadorService;

    @GetMapping("/jugadores")
    public String listarJugadoresPublico(Model model) {
        List<Jugador> jugadores = jugadorRepository.findAll();
        model.addAttribute("jugadores", jugadores);
        return "jugadores/list";
    }

    @PostMapping("/jugadores")
    public String guardar(@ModelAttribute Jugador jugador) {
        jugadorService.save(jugador);
        return "redirect:/admin/jugadores";
    }

    @PostMapping("/jugadores/editar/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute Jugador jugador) {
        jugador.setId(id);
        jugadorService.save(jugador);
        return "redirect:/admin/jugadores";
    }

    @GetMapping("/jugadores/estadistica/{id}")
    public String estadisticasJugador(@PathVariable Long id, Model model) {
        JugadorService.EstadisticasJugador stats = jugadorService.obtenerEstadisticas(id);
        if (stats == null) {
            return "redirect:/jugadores";
        }
        // Preparo los arrays para las gráficas:
        List<String> labels = new ArrayList<>();
        List<Integer> tacklesPrimera = new ArrayList<>();
        List<Integer> tacklesNoPrimera = new ArrayList<>();
        List<Integer> puntosPrimera = new ArrayList<>();
        List<Integer> puntosNoPrimera = new ArrayList<>();

        for (JugadorService.EstadisticasJugador.StatPorPartido p : stats.partidos) {
            String fecha = "";
            if (p.fecha != null) {
                fecha = (p.fecha.getDate() < 10 ? "0" : "") + p.fecha.getDate() + "/" +
                        ((p.fecha.getMonth() + 1) < 10 ? "0" : "") + (p.fecha.getMonth() + 1) + "/" +
                        (p.fecha.getYear() + 1900);
            }
            labels.add(fecha);

            if (p.esPrimera) {
                tacklesPrimera.add(p.tackles);
                puntosPrimera.add(p.tries * 5 + p.conversiones * 2 + p.drops * 3 + p.penales * 3);
                tacklesNoPrimera.add(null);
                puntosNoPrimera.add(null);
            } else {
                tacklesPrimera.add(null);
                puntosPrimera.add(null);
                tacklesNoPrimera.add(p.tackles);
                puntosNoPrimera.add(p.tries * 5 + p.conversiones * 2 + p.drops * 3 + p.penales * 3);
            }
        }
        model.addAttribute("stats", stats);
        model.addAttribute("labelsJs", labels);
        model.addAttribute("tacklesPrimeraJs", tacklesPrimera);
        model.addAttribute("tacklesNoPrimeraJs", tacklesNoPrimera);
        model.addAttribute("puntosPrimeraJs", puntosPrimera);
        model.addAttribute("puntosNoPrimeraJs", puntosNoPrimera);

        return "jugadores/estadistica";
    }
}
