package es.safareyes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.safareyes.Interfaces.IValoracionRepository;
import es.safareyes.modelos.Valoracion;

@SpringBootTest
@Transactional // perfilOrigen, perfilDestino y solicitud son LAZY
class TestRepositorioValoracion {

    @Autowired
    private IValoracionRepository repository;

    @Test
    void consultarTodos() {
        List<Valoracion> todas = repository.findAll();
        assertThat(todas).isNotNull(); // breakpoint aquí
    }
}