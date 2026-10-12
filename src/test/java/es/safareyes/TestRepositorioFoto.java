package es.safareyes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.safareyes.Interfaces.IFotoRepository;
import es.safareyes.modelos.Foto;

@SpringBootTest
@Transactional // necesario: prenda es LAZY
class TestRepositorioFoto {

    @Autowired
    private IFotoRepository repository;

    @Test
    void consultarTodos() {
        List<Foto> todas = repository.findAll();
        assertThat(todas).isNotNull(); // breakpoint aquí
    }
}