package ar.com.almafuerte2026.controller;

import ar.com.almafuerte2026.service.ParticipacionJuegoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ParticipacionJuegoService participacionJuegoService;

    public HomeController(ParticipacionJuegoService participacionJuegoService) {
        this.participacionJuegoService = participacionJuegoService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {

        // Rankings tackles
        model.addAttribute("tacklesPrimera", participacionJuegoService.rankingTacklesPrimera());
        model.addAttribute("tacklesNoPrimera", participacionJuegoService.rankingTacklesNoPrimera());

        // Rankings puntos
        model.addAttribute("puntosPrimera", participacionJuegoService.rankingPuntosPrimera());
        model.addAttribute("puntosNoPrimera", participacionJuegoService.rankingPuntosNoPrimera());

        // Rankings tarjetas
        model.addAttribute("tarjetasPrimera", participacionJuegoService.rankingTarjetasPrimera());
        model.addAttribute("tarjetasNoPrimera", participacionJuegoService.rankingTarjetasNoPrimera());

        // Rankings tries
        model.addAttribute("triesPrimera", participacionJuegoService.rankingTriesPrimera());
        model.addAttribute("triesNoPrimera", participacionJuegoService.rankingTriesNoPrimera());
        model.addAttribute("triesGeneral" , participacionJuegoService.rankingTriesGeneral());

        return "index";
    }
}
