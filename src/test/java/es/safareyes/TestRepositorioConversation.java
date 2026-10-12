package es.safareyes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.safareyes.Interfaces.IConversacionRepository;
import es.safareyes.modelos.Conversacion;

@SpringBootTest
@Transactional // mantiene la sesión abierta para las relaciones LAZY (perfil1, perfil2)
class TestRepositorioConversation {

    @Autowired
    private IConversacionRepository repository;

    @Test
    void consultarTodos() {
        List<Conversacion> todas = repository.findAll();
        assertThat(todas).isNotNull(); // breakpoint aquí para ver "todas"
    }
}