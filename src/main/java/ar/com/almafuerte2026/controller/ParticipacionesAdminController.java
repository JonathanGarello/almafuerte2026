package ar.com.almafuerte2026.controller;

import ar.com.almafuerte2026.model.Juego;
import ar.com.almafuerte2026.model.ParticipacionJuego;
import ar.com.almafuerte2026.repository.JuegoRepository;
import ar.com.almafuerte2026.repository.ParticipacionJuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/participaciones")
public class ParticipacionesAdminController {

    @Autowired
    private JuegoRepository juegoRepository;
    @Autowired
    private ParticipacionJuegoRepository participacionJuegoRepository;

    @GetMapping
    public String participaciones(@RequestParam(required = false) Long juegoId, Model model) {
        List<Juego> juegos = juegoRepository.findAll();
        model.addAttribute("juegos", juegos);

        if (juegoId != null) {
            List<ParticipacionJuego> participaciones = participacionJuegoRepository.findAllByJuego_Id(juegoId);
            model.addAttribute("participaciones", participaciones);
            model.addAttribute("juegoSel", juegoId);
        }
        return "admin/participaciones";
    }

    @PostMapping
    public String guardarStats(@RequestParam Long juegoId, @RequestParam Map<String, String> params) {
        List<ParticipacionJuego> participaciones = participacionJuegoRepository.findAllByJuego_Id(juegoId);
        for (ParticipacionJuego p : participaciones) {
            p.setTries(parseInt(params, "tries_" + p.getId()));
            p.setConversiones(parseInt(params, "conversiones_" + p.getId()));
            p.setDrops(parseInt(params, "drops_" + p.getId()));
            p.setPenales(parseInt(params, "penales_" + p.getId()));
            p.setTackles(parseInt(params, "tackles_" + p.getId()));
            participacionJuegoRepository.save(p);
        }
        return "redirect:/admin/participaciones";
    }

    private int parseInt(Map<String, String> params, String key) {
        try {
            return Integer.parseInt(params.getOrDefault(key, "0"));
        } catch (Exception e) {
            return 0;
        }
    }
}
