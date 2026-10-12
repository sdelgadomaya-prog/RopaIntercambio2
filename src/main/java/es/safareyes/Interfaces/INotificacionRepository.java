package es.safareyes.Interfaces;

import es.safareyes.modelos.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface INotificacionRepository extends JpaRepository<Notificacion, Long> {
}