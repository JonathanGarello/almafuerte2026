package ar.com.almafuerte2026.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resultado;
    private String rival;
    private boolean esPrimera;
    private LocalDate fecha;

    public boolean getEsPrimera() {
        return this.esPrimera;
    }
}
