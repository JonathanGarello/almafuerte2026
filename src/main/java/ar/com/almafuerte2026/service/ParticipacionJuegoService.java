package ar.com.almafuerte2026.service;

import ar.com.almafuerte2026.model.ParticipacionJuego;
import ar.com.almafuerte2026.repository.ParticipacionJuegoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipacionJuegoService {

    private final ParticipacionJuegoRepository participacionJuegoRepository;

    public ParticipacionJuegoService(ParticipacionJuegoRepository participacionJuegoRepository) {
        this.participacionJuegoRepository = participacionJuegoRepository;
    }

    public ParticipacionJuego save(ParticipacionJuego participacionJuego) {
        return participacionJuegoRepository.save(participacionJuego);
    }

    public Optional<ParticipacionJuego> findById(Long id) {
        return participacionJuegoRepository.findById(id);
    }

    public List<ParticipacionJuego> findAll() {
        return participacionJuegoRepository.findAll();
    }

    public void deleteById(Long id) {
        participacionJuegoRepository.deleteById(id);
    }

    // Rankings de tackles
    public List<ParticipacionJuegoRepository.RankingTackles> rankingTacklesPrimera() {
        return participacionJuegoRepository.rankingTacklesPrimera()
                .stream()
                .filter(item -> item.getTackles() > 0)
                .toList();
    }

    public List<ParticipacionJuegoRepository.RankingTackles> rankingTacklesNoPrimera() {
        return participacionJuegoRepository.rankingTacklesNoPrimera().stream()
                .filter(item -> item.getTackles() > 0)
                .toList();
    }

    public List<ParticipacionJuegoRepository.RankingTackles> rankingTacklesGeneral() {
        return participacionJuegoRepository.rankingTacklesGeneral().stream()
                .filter(item -> item.getTackles() > 0)
                .toList();
    }

    // Rankings de puntos
    public List<ParticipacionJuegoRepository.RankingPuntos> rankingPuntosPrimera() {
        return participacionJuegoRepository.rankingPuntosPrimera()
                .stream()
                .filter(item -> item.getPuntos() > 0)
                .toList();
    }

    public List<ParticipacionJuegoRepository.RankingPuntos> rankingPuntosNoPrimera() {
        return participacionJuegoRepository.rankingPuntosNoPrimera().stream()
                .filter(item -> item.getPuntos() > 0)
                .toList();
    }

    public List<ParticipacionJuegoRepository.RankingPuntos> rankingPuntosGeneral() {
        return participacionJuegoRepository.rankingPuntosGeneral().stream()
                .filter(item -> item.getPuntos() > 0)
                .toList();
    }

    // Rankings de tarjetas (amarillas + rojas)
    public List<ParticipacionJuegoRepository.RankingTarjetas> rankingTarjetasPrimera() {
        return participacionJuegoRepository.rankingTarjetasPrimera()
                .stream()
                .filter(item -> item.getTarjetas() != null && item.getTarjetas() > 0)
                .toList();
    }

    public List<ParticipacionJuegoRepository.RankingTarjetas> rankingTarjetasNoPrimera() {
        return participacionJuegoRepository.rankingTarjetasNoPrimera()
                .stream()
                .filter(item -> item.getTarjetas() != null && item.getTarjetas() > 0)
                .toList();
    }
}
