package ar.com.almafuerte2026.repository;

import ar.com.almafuerte2026.model.ParticipacionJuego;
import ar.com.almafuerte2026.model.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ParticipacionJuegoRepository extends JpaRepository<ParticipacionJuego, Long> {

    // Proyección para ranking de tackles
    interface RankingTackles {
        Jugador getJugador();
        Long getTackles();
    }

    // Proyección para ranking de puntos
    interface RankingPuntos {
        Jugador getJugador();
        Long getPuntos();
    }

    // Ranking tackles para "primera"
    @Query("SELECT p.jugador AS jugador, SUM(p.tackles) AS tackles " +
           "FROM ParticipacionJuego p " +
           "WHERE p.juego.esPrimera = true " +
           "GROUP BY p.jugador " +
           "ORDER BY SUM(p.tackles) DESC")
    List<RankingTackles> rankingTacklesPrimera();

    // Ranking tackles para "no primera"
    @Query("SELECT p.jugador AS jugador, SUM(p.tackles) AS tackles " +
           "FROM ParticipacionJuego p " +
           "WHERE p.juego.esPrimera = false " +
           "GROUP BY p.jugador " +
           "ORDER BY SUM(p.tackles) DESC")
    List<RankingTackles> rankingTacklesNoPrimera();

    // Ranking tackles general
    @Query("SELECT p.jugador AS jugador, SUM(p.tackles) AS tackles " +
           "FROM ParticipacionJuego p " +
           "GROUP BY p.jugador " +
           "ORDER BY SUM(p.tackles) DESC")
    List<RankingTackles> rankingTacklesGeneral();

    // Ranking puntos para "primera"
    @Query("SELECT p.jugador AS jugador, " +
           "SUM((p.tries*5) + (p.conversiones*2) + (p.drops*3) + (p.penales*3)) AS puntos " +
           "FROM ParticipacionJuego p " +
           "WHERE p.juego.esPrimera = true " +
           "GROUP BY p.jugador " +
           "ORDER BY puntos DESC")
    List<RankingPuntos> rankingPuntosPrimera();

    // Ranking puntos para "no primera"
    @Query("SELECT p.jugador AS jugador, " +
           "SUM((p.tries*5) + (p.conversiones*2) + (p.drops*3) + (p.penales*3)) AS puntos " +
           "FROM ParticipacionJuego p " +
           "WHERE p.juego.esPrimera = false " +
           "GROUP BY p.jugador " +
           "ORDER BY puntos DESC")
    List<RankingPuntos> rankingPuntosNoPrimera();

    // Ranking puntos general
    @Query("SELECT p.jugador AS jugador, " +
           "SUM((p.tries*5) + (p.conversiones*2) + (p.drops*3) + (p.penales*3)) AS puntos " +
           "FROM ParticipacionJuego p " +
           "GROUP BY p.jugador " +
           "ORDER BY puntos DESC")
    List<RankingPuntos> rankingPuntosGeneral();

    // Nuevo: Obtener participaciones por juego
    List<ParticipacionJuego> findAllByJuego_Id(Long juegoId);
}
