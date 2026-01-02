package ar.com.almafuerte2026.controller;

import ar.com.almafuerte2026.model.Juego;
import ar.com.almafuerte2026.model.ParticipacionJuego;
import ar.com.almafuerte2026.repository.JuegoRepository;
import ar.com.almafuerte2026.repository.ParticipacionJuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.Comparator;

@Controller
public class JuegoController {
    @Autowired
    private JuegoRepository juegoRepository;

    @Autowired
    private ParticipacionJuegoRepository participacionJuegoRepository;

    @GetMapping("/juegos")
    public String listarJuegosPublico(Model model) {
        List<Juego> todos = juegoRepository.findAll();
        List<Juego> primera = todos.stream().filter(Juego::getEsPrimera).collect(Collectors.toList());
        List<Juego> noPrimera = todos.stream().filter(j -> !j.getEsPrimera()).collect(Collectors.toList());
        model.addAttribute("juegosPrimera", primera);
        model.addAttribute("juegosNoPrimera", noPrimera);
        return "juegos/list";
    }

    @GetMapping("/juegos/estadistica/{id}")
    public String estadisticaJuego(@PathVariable("id") Long id, Model model) {
        Optional<Juego> juegoOpt = juegoRepository.findById(id);
        if (juegoOpt.isEmpty()) {
            return "redirect:/juegos";
        }
        Juego juego = juegoOpt.get();
        List<ParticipacionJuego> participaciones = participacionJuegoRepository.findAllByJuego_Id(id);

        // Calcular totales del partido actual
        int totalTries = participaciones.stream().mapToInt(ParticipacionJuego::getTries).sum();
        int totalTackles = participaciones.stream().mapToInt(ParticipacionJuego::getTackles).sum();
        int totalConversiones = participaciones.stream().mapToInt(ParticipacionJuego::getConversiones).sum();
        int totalDrops = participaciones.stream().mapToInt(ParticipacionJuego::getDrops).sum();
        int totalPenales = participaciones.stream().mapToInt(ParticipacionJuego::getPenales).sum();

        // Calcular puntos del partido actual
        int totalPuntos = totalTries*5 + totalConversiones*2 + totalDrops*3 + totalPenales*3;

        // Buscar partido anterior
        List<Juego> juegosMismoTipo = juegoRepository.findAll().stream()
            .filter(j -> j.getEsPrimera() == juego.getEsPrimera())
            .filter(j -> j.getFecha()!=null && juego.getFecha()!=null && j.getFecha().isBefore(juego.getFecha()))
            .sorted(Comparator.comparing(Juego::getFecha).reversed())
            .collect(Collectors.toList());

        boolean esPrimerPartido = juegosMismoTipo.isEmpty();

        int totalTriesAnterior = 0;
        int totalPuntosAnterior = 0;
        if (!esPrimerPartido) {
            Juego anterior = juegosMismoTipo.get(0);
            List<ParticipacionJuego> partAnt = participacionJuegoRepository.findAllByJuego_Id(anterior.getId());
            totalTriesAnterior = partAnt.stream().mapToInt(ParticipacionJuego::getTries).sum();
            int conversionesAnt = partAnt.stream().mapToInt(ParticipacionJuego::getConversiones).sum();
            int dropsAnt = partAnt.stream().mapToInt(ParticipacionJuego::getDrops).sum();
            int penalesAnt = partAnt.stream().mapToInt(ParticipacionJuego::getPenales).sum();
            totalPuntosAnterior = totalTriesAnterior*5 + conversionesAnt*2 + dropsAnt*3 + penalesAnt*3;
        }

        // Calcular variación tries %
        String variacionTries = "";
        if(esPrimerPartido) {
            variacionTries = null; // Será manejado en la vista
        } else if(totalTriesAnterior == 0 && totalTries > 0) {
            variacionTries = "+100";
        } else if (totalTriesAnterior == 0) {
            variacionTries = "0";
        } else {
            int diff = totalTries - totalTriesAnterior;
            double perc = ((double)diff / Math.abs(totalTriesAnterior))*100.0;
            int redondeado = (int)Math.round(perc);
            variacionTries = (redondeado > 0 ? "+" : "") + redondeado;
        }

        // Calcular variación puntos %
        String variacionPuntos = "";
        if(esPrimerPartido) {
            variacionPuntos = null; // Será manejado en la vista
        } else if(totalPuntosAnterior == 0 && totalPuntos > 0) {
            variacionPuntos = "+100";
        } else if (totalPuntosAnterior == 0) {
            variacionPuntos = "0";
        } else {
            int diff = totalPuntos - totalPuntosAnterior;
            double perc = ((double)diff / Math.abs(totalPuntosAnterior))*100.0;
            int redondeado = (int)Math.round(perc);
            variacionPuntos = (redondeado > 0 ? "+" : "") + redondeado;
        }

        // =========================
        // Nueva lógica para estabilidad de gráfica de porcentaje de puntos/tackles
        // =========================
        // Lista de partidos del mismo tipo de equipo, ordenados por fecha (ascendente para la evolución)
        List<Juego> partidosEvolucion = juegoRepository.findAll().stream()
            .filter(j -> j.getEsPrimera() == juego.getEsPrimera())
            .filter(j -> j.getFecha() != null)
            .sorted(Comparator.comparing(Juego::getFecha))
            .collect(Collectors.toList());

        List<String> labelsJs = partidosEvolucion.stream()
            .map(jg -> {
                LocalDate f = jg.getFecha();
                return f != null ? String.format("%02d/%02d/%d", f.getDayOfMonth(), f.getMonthValue(), f.getYear()) : jg.getRival();
            })
            .collect(Collectors.toList());

        List<Integer> varTacklesJs = new java.util.ArrayList<>();
        List<Integer> varPuntosJs = new java.util.ArrayList<>();
        int prevTackles = 0, prevPuntos = 0;
        boolean firstLoop = true;
        for (Juego jg : partidosEvolucion) {
            List<ParticipacionJuego> parts = participacionJuegoRepository.findAllByJuego_Id(jg.getId());
            int tackles = parts.stream().mapToInt(ParticipacionJuego::getTackles).sum();
            int tries = parts.stream().mapToInt(ParticipacionJuego::getTries).sum();
            int conv = parts.stream().mapToInt(ParticipacionJuego::getConversiones).sum();
            int drops = parts.stream().mapToInt(ParticipacionJuego::getDrops).sum();
            int penales = parts.stream().mapToInt(ParticipacionJuego::getPenales).sum();
            int puntos = tries*5 + conv*2 + drops*3 + penales*3;

            if (firstLoop) {
                varTacklesJs.add(null);
                varPuntosJs.add(null);
                firstLoop = false;
            } else {
                int diffTack = tackles - prevTackles;
                int percTack = (prevTackles != 0) ? (int)Math.round((double)diffTack / Math.abs(prevTackles) * 100.0) : (tackles>0 ? 100 : 0);
                varTacklesJs.add(percTack);
                int diffPunt = puntos - prevPuntos;
                int percPunt = (prevPuntos != 0) ? (int)Math.round((double)diffPunt / Math.abs(prevPuntos) * 100.0) : (puntos>0 ? 100 : 0);
                varPuntosJs.add(percPunt);
            }
            prevTackles = tackles;
            prevPuntos = puntos;
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("juego", juego);
        stats.put("totalTries", totalTries);
        stats.put("totalTackles", totalTackles);
        stats.put("totalConversiones", totalConversiones);
        stats.put("totalDrops", totalDrops);
        stats.put("totalPenales", totalPenales);
        stats.put("totalPuntos", totalPuntos);
        stats.put("participaciones", participaciones);
        stats.put("variacionTries", variacionTries);
        stats.put("variacionPuntos", variacionPuntos);
        stats.put("esPrimerPartido", esPrimerPartido);

        // Pasar los datos para la gráfica
        stats.put("labelsJs", labelsJs);
        stats.put("varTacklesJs", varTacklesJs);
        stats.put("varPuntosJs", varPuntosJs);

        model.addAttribute("stats", stats);
        return "juegos/estadistica";
    }
}
