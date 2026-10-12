package es.safareyes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import es.safareyes.dta.ConversacionProyeccion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.safareyes.Interfaces.IMensajeRepository;
import es.safareyes.modelos.Mensaje;

@SpringBootTest
@Transactional
class TestRepositoryMensaje {

    @Autowired
    private IMensajeRepository repository;

    @Test
    void consultarTodos() {
        List<Mensaje> todos = repository.findAll();
        assertThat(todos).isNotNull(); // breakpoint aquí para ver "todos"
    }

    //consulta nº7
    @Test
    void consultarConversacion() {
        List<ConversacionProyeccion> lista = repository.obtenerConversacion(1L);
        System.out.println(" CONVERSACIÓN DE LA SOLICITUD 1 (" + lista.size() + " mensajes)");
        lista.forEach(m -> System.out.println(
                m.getFechaEnvio() + " " + m.getHoraEnvio() + " | "
                        + m.getEmisor() + " -> " + m.getReceptor() + ": " + m.getContenido()));
    }
}