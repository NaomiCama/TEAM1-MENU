package app.infrastructure.item.menu.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "Menus")
public class MenuDto {
    @Id
    private Long menuid;
    private String nombrem;
    private String estado;

}
