package es.safareyes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.safareyes.Interfaces.IEstiloRepository;
import es.safareyes.modelos.Estilo;

@SpringBootTest
@Transactional
class TestRepositorioEstilo {

    @Autowired
    private IEstiloRepository repository;

    @Test
    void consultarTodos() {
        List<Estilo> todos = repository.findAll();
        assertThat(todos).isNotNull(); // breakpoint aquí para ver "todos"
    }
}