package app.infrastructure.item.comida.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SeleccionDto {

    private Long menuid;
    private String nombremenu;
    private String nombrecomida;
    private Double precio;
    private String categoria;


}
