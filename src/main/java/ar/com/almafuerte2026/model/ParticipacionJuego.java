package ar.com.almafuerte2026.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipacionJuego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Jugador jugador;

    @ManyToOne(optional = false)
    private Juego juego;

    private int tries;
    private int conversiones;
    private int drops;
    private int penales;
    private int tackles;
}
