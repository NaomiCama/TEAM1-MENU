package app.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@Getter @Setter
public class Reserva {

    private int id_reserva;
    private String nombre;
    private String correo;
    private LocalTime hora_reserva;
    private int num_personas;
    private String situacion;
    private double monto_total;
    private String estado;
    private List<ReservaDetalle> reserva_detalle;

    @Getter @Setter
    public static class ReservaDetalle {

        private int id;
        private int idReserva;
        private int comidaid;
        private String nombrePedido;
        private int cantidad;
        private String estado;

    }

}
