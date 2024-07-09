package app.infrastructure.item.reserva.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalTime;

@Getter
@Setter
@Table(name = "RESERVADETALLE")
public class ReservaDetalleDto {

    @Id
    private Integer id;
    private Integer id_reserva;
    private Integer comidaid;
    private String nombrepedido;
    private Integer cantidad;
    private String estado;

 }