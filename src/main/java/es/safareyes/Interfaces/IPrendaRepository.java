package es.safareyes.Interfaces;

import es.safareyes.dta.ConsultaPrendaDetalleProyeccion;
import es.safareyes.dta.ConsultaPrendasPagProyeccion;
import es.safareyes.dta.InformeActividadProyeccion;
import es.safareyes.modelos.Prenda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IPrendaRepository extends JpaRepository<Prenda, Long> {


    //1ºconsulta
    @Query(nativeQuery = true, value = """
        SELECT pr.id_prenda AS "idPrenda", pr.titulo AS titulo, pr.marca AS marca,
               pr.talla AS talla, pr.estado AS estado, c.nombre AS categoria,
               pe.nombre AS propietaria, pe.ciudad AS ciudad,
               pr.permite_prestamo AS "permitePrestamo",
               pr.permite_intercambio AS "permiteIntercambio",
               pr.fecha_publicacion AS "fechaPublicacion"
        FROM prenda pr
            JOIN categoria c ON c.id_categoria = pr.id_categoria
            JOIN perfil pe ON pe.id_perfil = pr.id_perfil
        WHERE pr.disponibilidad = TRUE
          AND (CAST(:categoria AS BIGINT) IS NULL OR c.id_categoria = :categoria)
          AND (CAST(:texto AS TEXT) IS NULL
               OR LOWER(pr.titulo) LIKE LOWER(CONCAT('%', CAST(:texto AS TEXT), '%')))
          AND (CAST(:talla AS TEXT) IS NULL OR pr.talla = :talla)
        ORDER BY pr.fecha_publicacion DESC
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM prenda pr
            JOIN categoria c ON c.id_categoria = pr.id_categoria
        WHERE pr.disponibilidad = TRUE
          AND (CAST(:categoria AS BIGINT) IS NULL OR c.id_categoria = :categoria)
          AND (CAST(:texto AS TEXT) IS NULL
               OR LOWER(pr.titulo) LIKE LOWER(CONCAT('%', CAST(:texto AS TEXT), '%')))
          AND (CAST(:talla AS TEXT) IS NULL OR pr.talla = :talla)
        """)
    Page<ConsultaPrendasPagProyeccion> obtenerPrendasPaginadas(
            @Param("categoria") Long categoria,
            @Param("texto") String texto,
            @Param("talla") String talla,
            Pageable pageable);


    //2ºconsulta

        @Query(nativeQuery = true, value =
                "SELECT " +
                        "  pr.id_prenda AS \"idPrenda\", " +
                        "  pr.titulo AS \"titulo\", " +
                        "  pr.descripcion AS \"descripcion\", " +
                        "  pr.marca AS \"marca\", " +
                        "  pr.talla AS \"talla\", " +
                        "  pr.estado AS \"estado\", " +
                        "  pr.disponibilidad AS \"disponibilidad\", " +
                        "  pr.permite_intercambio AS \"permiteIntercambio\", " +
                        "  pr.permite_prestamo AS \"permitePrestamo\", " +
                        "  pr.fecha_publicacion AS \"fechaPublicacion\", " +
                        "  c.nombre AS \"categoria\", " +
                        "  p.id_perfil AS \"idPropietario\", " +
                        "  CONCAT_WS(' ', p.nombre, p.apellido1, p.apellido2) AS \"propietario\", " +
                        "  (SELECT CAST(ROUND(AVG(v.puntuacion), 1) AS DOUBLE PRECISION) " +
                        "     FROM valoraciones v WHERE v.id_perfil_destino = p.id_perfil) AS \"mediaPuntuacion\", " +
                        "  (SELECT COUNT(*) FROM valoraciones v " +
                        "     WHERE v.id_perfil_destino = p.id_perfil) AS \"numValoraciones\", " +
                        "  (SELECT STRING_AGG(f.url, ', ' ORDER BY f.orden) " +
                        "     FROM foto f WHERE f.id_prenda = pr.id_prenda) AS \"fotos\" " +
                        "FROM prenda pr " +
                        "  JOIN categoria c ON pr.id_categoria = c.id_categoria " +
                        "  JOIN perfil p ON pr.id_perfil = p.id_perfil " +
                        "WHERE pr.id_prenda = :id")
        Optional<ConsultaPrendaDetalleProyeccion> obtenerDetallePrenda(@Param("id") Long id);

    //consulta nº8
    @Query(nativeQuery = true, value =
            "SELECT " +
                    "  TO_CHAR(a.fecha, 'YYYY-MM') AS \"mes\", " +
                    "  a.categoria AS \"categoria\", " +
                    "  SUM(a.publicaciones) AS \"publicaciones\", " +
                    "  SUM(a.intercambios) AS \"intercambios\", " +
                    "  SUM(a.prestamos) AS \"prestamos\" " +
                    "FROM ( " +
                    "  SELECT pr.fecha_publicacion AS fecha, c.nombre AS categoria, " +
                    "         1 AS publicaciones, 0 AS intercambios, 0 AS prestamos " +
                    "  FROM prenda pr " +
                    "    JOIN categoria c ON c.id_categoria = pr.id_categoria " +
                    "  UNION ALL " +
                    "  SELECT s.fecha_creacion AS fecha, c.nombre AS categoria, " +
                    "         0 AS publicaciones, " +
                    "         CASE WHEN s.tipo_solicitud = 'Intercambio' THEN 1 ELSE 0 END AS intercambios, " +
                    "         CASE WHEN s.tipo_solicitud = 'Prestamo' THEN 1 ELSE 0 END AS prestamos " +
                    "  FROM solicitudes s " +
                    "    JOIN solicitud_prenda sp ON sp.id_solicitud = s.id_solicitud " +
                    "    JOIN prenda pr ON pr.id_prenda = sp.id_prenda " +
                    "    JOIN categoria c ON c.id_categoria = pr.id_categoria " +
                    ") a " +
                    "WHERE a.fecha >= CAST(:desde AS DATE) " +
                    "  AND a.fecha < CAST(:hasta AS DATE) + 1 " +
                    "GROUP BY TO_CHAR(a.fecha, 'YYYY-MM'), a.categoria " +
                    "ORDER BY \"mes\" DESC, \"categoria\" ASC")
    List<InformeActividadProyeccion> obtenerInformeActividad(
            @Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
    }
