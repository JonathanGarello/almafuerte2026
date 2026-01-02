package ar.com.almafuerte2026.controller;

import ar.com.almafuerte2026.service.ParticipacionJuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/participaciones")
public class ParticipacionJuegoController {

    private final ParticipacionJuegoService participacionJuegoService;

    public ParticipacionJuegoController(ParticipacionJuegoService participacionJuegoService) {
        this.participacionJuegoService = participacionJuegoService;
    }

    @GetMapping
    public String listParticipaciones(Model model) {
        model.addAttribute("participaciones", participacionJuegoService.findAll());
        return "participaciones/list";
    }

    @GetMapping("/{id}")
    public String showParticipacion(@PathVariable Long id, Model model) {
        model.addAttribute("participacion", participacionJuegoService.findById(id).orElse(null));
        return "participaciones/detail";
    }

    @GetMapping("/nuevo")
    public String createParticipacionForm(Model model) {
        //model.addAttribute("participacion", new ParticipacionJuego());
        return "participaciones/form";
    }

    @PostMapping
    public String saveParticipacion(/*@ModelAttribute ParticipacionJuego participacion*/) {
        //participacionJuegoService.save(participacion);
        return "redirect:/participaciones";
    }

    // Endpoints ranking tackles
    @GetMapping("/ranking/tackles/primera")
    public String rankingTacklesPrimera(Model model) {
        model.addAttribute("ranking", participacionJuegoService.rankingTacklesPrimera());
        return "participaciones/ranking_tackles_primera";
    }

    @GetMapping("/ranking/tackles/noprimera")
    public String rankingTacklesNoPrimera(Model model) {
        model.addAttribute("ranking", participacionJuegoService.rankingTacklesNoPrimera());
        return "participaciones/ranking_tackles_noprimera";
    }

    @GetMapping("/ranking/tackles/general")
    public String rankingTacklesGeneral(Model model) {
        model.addAttribute("ranking", participacionJuegoService.rankingTacklesGeneral());
        return "participaciones/ranking_tackles_general";
    }

    // Endpoints ranking puntos
    @GetMapping("/ranking/puntos/primera")
    public String rankingPuntosPrimera(Model model) {
        model.addAttribute("ranking", participacionJuegoService.rankingPuntosPrimera());
        return "participaciones/ranking_puntos_primera";
    }

    @GetMapping("/ranking/puntos/noprimera")
    public String rankingPuntosNoPrimera(Model model) {
        model.addAttribute("ranking", participacionJuegoService.rankingPuntosNoPrimera());
        return "participaciones/ranking_puntos_noprimera";
    }

    @GetMapping("/ranking/puntos/general")
    public String rankingPuntosGeneral(Model model) {
        model.addAttribute("ranking", participacionJuegoService.rankingPuntosGeneral());
        return "participaciones/ranking_puntos_general";
    }
}
