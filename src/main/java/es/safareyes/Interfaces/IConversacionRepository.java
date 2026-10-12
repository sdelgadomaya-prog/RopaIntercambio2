package es.safareyes.Interfaces;

import es.safareyes.modelos.Conversacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IConversacionRepository extends JpaRepository<Conversacion, Long> {
}