package ar.com.almafuerte2026.service;

import ar.com.almafuerte2026.model.Jugador;
import ar.com.almafuerte2026.model.ParticipacionJuego;
import ar.com.almafuerte2026.model.Juego;
import ar.com.almafuerte2026.repository.JugadorRepository;
import ar.com.almafuerte2026.repository.ParticipacionJuegoRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;
    private final ParticipacionJuegoRepository participacionJuegoRepository;

    public JugadorService(JugadorRepository jugadorRepository, ParticipacionJuegoRepository participacionJuegoRepository) {
        this.jugadorRepository = jugadorRepository;
        this.participacionJuegoRepository = participacionJuegoRepository;
    }

    public Jugador save(Jugador jugador) {
        return jugadorRepository.save(jugador);
    }

    public Optional<Jugador> findById(Long id) {
        return jugadorRepository.findById(id);
    }

    public List<Jugador> findAll() {
        return jugadorRepository.findAll();
    }

    public void deleteById(Long id) {
        jugadorRepository.deleteById(id);
    }

    // DTO para los datos de stats generales y por partido
    public static class EstadisticasJugador {
        public Jugador jugador;
        public int juegosTotales;
        public int juegosPrimera;
        public int juegosNoPrimera;
        public int tackles;
        public int tries;
        public int drops;
        public int penales;
        public int conversiones;

        public List<StatPorPartido> partidos;

        // Rankings agregados
        public int rankingTacklesPrimera;
        public int rankingTacklesNoPrimera;
        public int rankingPuntosPrimera;
        public int rankingPuntosNoPrimera;

        public static class StatPorPartido {
            public String rival;
            public Date fecha;
            public boolean esPrimera;
            public int tackles;
            public int tries;
            public int drops;
            public int penales;
            public int conversiones;
            public String resultado;
        }
    }

    public EstadisticasJugador obtenerEstadisticas(Long jugadorId) {
        EstadisticasJugador resultado = new EstadisticasJugador();
        Optional<Jugador> optJugador = jugadorRepository.findById(jugadorId);
        if (optJugador.isEmpty()) return null;
        resultado.jugador = optJugador.get();

        List<ParticipacionJuego> participaciones = participacionJuegoRepository.findAll()
                .stream()
                .filter(p -> Objects.equals(p.getJugador().getId(), jugadorId))
                .collect(Collectors.toList());
        resultado.juegosTotales = participaciones.size();
        resultado.juegosPrimera = (int) participaciones.stream().filter(p -> p.getJuego().getEsPrimera()).count();
        resultado.juegosNoPrimera = (int) participaciones.stream().filter(p -> !p.getJuego().getEsPrimera()).count();
        resultado.tackles = participaciones.stream().mapToInt(ParticipacionJuego::getTackles).sum();
        resultado.tries = participaciones.stream().mapToInt(ParticipacionJuego::getTries).sum();
        resultado.drops = participaciones.stream().mapToInt(ParticipacionJuego::getDrops).sum();
        resultado.penales = participaciones.stream().mapToInt(ParticipacionJuego::getPenales).sum();
        resultado.conversiones = participaciones.stream().mapToInt(ParticipacionJuego::getConversiones).sum();

        resultado.partidos = participaciones.stream().map(p -> {
            EstadisticasJugador.StatPorPartido s = new EstadisticasJugador.StatPorPartido();
            Juego j = p.getJuego();
            s.rival = j.getRival();
            s.fecha = java.sql.Date.valueOf(j.getFecha());
            s.esPrimera = j.getEsPrimera();
            s.resultado = j.getResultado();
            s.tackles = p.getTackles();
            s.tries = p.getTries();
            s.drops = p.getDrops();
            s.penales = p.getPenales();
            s.conversiones = p.getConversiones();
            return s;
        }).sorted(Comparator.comparing(s -> s.fecha)).collect(Collectors.toList());

        // Calculo de rankings
        List<Jugador> todosLosJugadores = jugadorRepository.findAll();
        // Para resultados (jugadorId, tackles, puntos)
        class Result {
            Long id;
            int tacklesPrimera;
            int tacklesNoPrimera;
            int puntosPrimera;
            int puntosNoPrimera;
            Result(Long id) { this.id = id; }
        }
        List<Result> resultados = new ArrayList<>();
        for (Jugador j : todosLosJugadores) {
            List<ParticipacionJuego> par = participacionJuegoRepository.findAll().stream()
                .filter(p -> Objects.equals(p.getJugador().getId(), j.getId()))
                .collect(Collectors.toList());
            Result r = new Result(j.getId());
            r.tacklesPrimera = par.stream().filter(p -> p.getJuego().getEsPrimera()).mapToInt(ParticipacionJuego::getTackles).sum();
            r.tacklesNoPrimera = par.stream().filter(p -> !p.getJuego().getEsPrimera()).mapToInt(ParticipacionJuego::getTackles).sum();
            r.puntosPrimera = par.stream().filter(p -> p.getJuego().getEsPrimera()).mapToInt(p -> p.getTries()*5 + p.getConversiones()*2 + p.getDrops()*3 + p.getPenales()*3).sum();
            r.puntosNoPrimera = par.stream().filter(p -> !p.getJuego().getEsPrimera()).mapToInt(p -> p.getTries()*5 + p.getConversiones()*2 + p.getDrops()*3 + p.getPenales()*3).sum();
            resultados.add(r);
        }
        // Rankings por tackles primera
        resultados.sort(Comparator.comparingInt((Result r) -> -r.tacklesPrimera));
        resultado.rankingTacklesPrimera = 1 + resultados.indexOf(resultados.stream().filter(r -> r.id.equals(jugadorId)).findFirst().orElse(null));
        // Tackles no primera
        resultados.sort(Comparator.comparingInt((Result r) -> -r.tacklesNoPrimera));
        resultado.rankingTacklesNoPrimera = 1 + resultados.indexOf(resultados.stream().filter(r -> r.id.equals(jugadorId)).findFirst().orElse(null));
        // Puntos primera
        resultados.sort(Comparator.comparingInt((Result r) -> -r.puntosPrimera));
        resultado.rankingPuntosPrimera = 1 + resultados.indexOf(resultados.stream().filter(r -> r.id.equals(jugadorId)).findFirst().orElse(null));
        // Puntos no primera
        resultados.sort(Comparator.comparingInt((Result r) -> -r.puntosNoPrimera));
        resultado.rankingPuntosNoPrimera = 1 + resultados.indexOf(resultados.stream().filter(r -> r.id.equals(jugadorId)).findFirst().orElse(null));

        return resultado;
    }
}
