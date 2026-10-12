package es.safareyes.Interfaces;

import es.safareyes.modelos.Estilo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstiloRepository extends JpaRepository<Estilo, Long> {
}