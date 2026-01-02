package ar.com.almafuerte2026.controller;

import ar.com.almafuerte2026.model.Juego;
import ar.com.almafuerte2026.repository.JuegoRepository;
import ar.com.almafuerte2026.repository.ParticipacionJuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/juegos")
public class AdminJuegosController {

    @Autowired
    private JuegoRepository juegoRepository;
    @Autowired
    private ParticipacionJuegoRepository participacionJuegoRepository;

    @GetMapping
    public String listaJuegos(Model model) {
        List<Juego> juegos = juegoRepository.findAll();
        model.addAttribute("juegos", juegos);
        return "admin/juegos";
    }

    @GetMapping("/editar/{id}")
    public String editarJuegoForm(@PathVariable Long id, Model model) {
        Optional<Juego> juegoOpt = juegoRepository.findById(id);
        if (juegoOpt.isPresent()) {
            model.addAttribute("juego", juegoOpt.get());
            return "admin/editar-juego";
        } else {
            return "redirect:/admin/juegos";
        }
    }

    @PostMapping("/editar/{id}")
    public String editarJuegoGuardar(@PathVariable Long id,
                                     @RequestParam String rival,
                                     @RequestParam String resultado,
                                     @RequestParam LocalDate fecha,
                                     @RequestParam String esPrimera) {
        Optional<Juego> juegoOpt = juegoRepository.findById(id);
        if (juegoOpt.isPresent()) {
            Juego juego = juegoOpt.get();
            juego.setRival(rival);
            juego.setResultado(resultado);
            juego.setFecha(fecha);
            juego.setEsPrimera(Boolean.parseBoolean(esPrimera));
            juegoRepository.save(juego);
        }
        return "redirect:/admin/juegos";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarJuego(@PathVariable Long id) {
        participacionJuegoRepository.deleteAll(participacionJuegoRepository.findAllByJuego_Id(id));
        juegoRepository.deleteById(id);
        return "redirect:/admin/juegos";
    }
}
