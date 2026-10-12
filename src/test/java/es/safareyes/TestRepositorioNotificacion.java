package es.safareyes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.safareyes.Interfaces.INotificacionRepository;
import es.safareyes.modelos.Notificacion;

@SpringBootTest
@Transactional // perfil y solicitud son LAZY
class TestRepositorioNotificacion {

    @Autowired
    private INotificacionRepository repository;

    @Test
    void consultarTodas() {
        List<Notificacion> todas = repository.findAll();
        System.out.println("Notificaciones encontradas: " + todas.size());
        assertThat(todas).isNotNull(); // breakpoint aquí
    }
}