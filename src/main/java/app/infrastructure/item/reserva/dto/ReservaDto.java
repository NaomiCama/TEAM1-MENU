package app.infrastructure.item.reserva.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalTime;

@Getter @Setter
@Table("RESERVA")
public class ReservaDto {

    @Id
    private Integer id_reserva ;
    private  String nombre;
    private  String correo;
    private LocalTime hora_reserva;
    private int num_personas;
    private String situacion;
    private  double monto_total;
    private String estado;

}
