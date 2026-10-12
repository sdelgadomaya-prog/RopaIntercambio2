package es.safareyes.Interfaces;

import es.safareyes.modelos.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFotoRepository extends JpaRepository<Foto, Long> {
}