package ar.com.almafuerte2026.service;

import ar.com.almafuerte2026.model.Juego;
import ar.com.almafuerte2026.repository.JuegoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JuegoService {

    private final JuegoRepository juegoRepository;

    public JuegoService(JuegoRepository juegoRepository) {
        this.juegoRepository = juegoRepository;
    }

    public Juego save(Juego juego) {
        return juegoRepository.save(juego);
    }

    public Optional<Juego> findById(Long id) {
        return juegoRepository.findById(id);
    }

    public List<Juego> findAll() {
        return juegoRepository.findAll();
    }

    public void deleteById(Long id) {
        juegoRepository.deleteById(id);
    }
}
