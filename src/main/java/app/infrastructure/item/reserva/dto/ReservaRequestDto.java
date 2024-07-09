package app.infrastructure.item.reserva.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class ReservaRequestDto {

    private String nombre;
    private String correo;
    private LocalTime hora_reserva;
    private int num_personas;
    private String situacion;
    private double monto_total;
    private String estado;
    private List<ReservaDetalleDto> reserva_detalle;

}