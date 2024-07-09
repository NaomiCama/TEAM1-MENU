package app.infrastructure.item.comida.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter @Setter
@Table(name = "Comidas")
public class ComidaDto {
    @Id
    private Long comidaid;
    //private Integer id_resturante;
    private String nombrec;
    private String categoria;
    private Double precio;
    private  Integer stock;
    private String image;
    private Long menuid;
    private String estado;


}

