package es.safareyes.Interfaces;
import es.safareyes.dtos.ConsultaCategoriaCantidadProyeccion;
import es.safareyes.modelos.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface ICategoriaRepository extends JpaRepository<Categoria, Integer> {

    //3º consulta
    @Query(nativeQuery = true, value =
            "SELECT " +
                    "  c.id_categoria AS \"idCategoria\", " +
                    "  c.nombre AS \"nombre\", " +
                    "  COUNT(p.id_prenda) AS \"prendasDisponibles\" " +
                    "FROM categoria c " +
                    "  LEFT JOIN prenda p ON p.id_categoria = c.id_categoria " +
                    "                    AND p.disponibilidad = TRUE " +
                    "GROUP BY c.id_categoria, c.nombre " +
                    "ORDER BY c.nombre ASC")
    List<ConsultaCategoriaCantidadProyeccion> obtenerCategoriasConCantidad();
}
