package es.safareyes.dta;

import java.time.LocalDateTime;

public interface ConsultaPrendasPagProyeccion {
    Long getIdPrenda();
    String getTitulo();
    String getMarca();
    String getTalla();
    String getEstado();
    String getCategoria();
    String getPropietaria();
    String getCiudad();
    Boolean getPermitePrestamo();
    Boolean getPermiteIntercambio();
    LocalDateTime getFechaPublicacion();
}