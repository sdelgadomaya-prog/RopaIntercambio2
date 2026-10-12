package es.safareyes.dta;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ConversacionProyeccion {
    Long getIdMensaje();
    Long getIdSolicitud();
    Long getIdEmisor();
    String getEmisor();
    Long getIdReceptor();
    String getReceptor();
    String getContenido();
    LocalDate getFechaEnvio();
    LocalTime getHoraEnvio();
}