package ar.com.almafuerte2026.controller;

import ar.com.almafuerte2026.model.Juego;
import ar.com.almafuerte2026.model.Jugador;
import ar.com.almafuerte2026.model.ParticipacionJuego;
import ar.com.almafuerte2026.repository.JuegoRepository;
import ar.com.almafuerte2026.repository.JugadorRepository;
import ar.com.almafuerte2026.repository.ParticipacionJuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/admin/crear-juego")
public class CrearJuegoController {

    @Autowired
    private JugadorRepository jugadorRepository;
    @Autowired
    private JuegoRepository juegoRepository;
    @Autowired
    private ParticipacionJuegoRepository participacionJuegoRepository;

    @GetMapping
    public String mostrarFormulario(Model model) {
        List<Jugador> jugadores = new ArrayList<>();
        jugadorRepository.findAll().forEach(jugadores::add);
        jugadores.sort(Comparator.comparingInt(j -> j.getPosicion().ordinal()));
        model.addAttribute("jugadores", jugadores);
        return "admin/crear-juego";
    }

    @PostMapping
    public String crearJuego(
            @RequestParam String rival,
            @RequestParam String resultado,
            @RequestParam LocalDate fecha,
            @RequestParam String esPrimera,
            @RequestParam(name = "jugadoresIds", required = false) String jugadoresIds
    ) {
        Juego juego = new Juego();
        juego.setRival(rival);
        juego.setResultado(resultado);
        juego.setFecha(fecha);
        juego.setEsPrimera(Boolean.parseBoolean(esPrimera));
        juegoRepository.save(juego);

        if (jugadoresIds != null && !jugadoresIds.isBlank()) {
            String[] idArr = jugadoresIds.split(",");
            for (String idStr : idArr) {
                try {
                    Long jugadorId = Long.valueOf(idStr);
                    Optional<Jugador> optJugador = jugadorRepository.findById(jugadorId);
                    if (optJugador.isPresent()) {
                        ParticipacionJuego pj = new ParticipacionJuego();
                        pj.setJuego(juego);
                        pj.setJugador(optJugador.get());
                        participacionJuegoRepository.save(pj);
                    }
                } catch (Exception ignored) {}
            }
        }

        return "redirect:/admin/dashboard";
    }
}
