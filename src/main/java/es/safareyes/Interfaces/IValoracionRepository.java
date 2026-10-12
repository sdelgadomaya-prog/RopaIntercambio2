package es.safareyes.Interfaces;

import es.safareyes.modelos.Valoracion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IValoracionRepository extends JpaRepository<Valoracion, Long> {
}