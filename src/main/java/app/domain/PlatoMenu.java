package app.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlatoMenu {


        private Integer id;
        private String nombre;
        private String categoria;
        private Double precio;
        private  Integer stock;
        private String image;
        private String estado;
        private MenuDetalle menu_detalle;


        @Data
        @Getter
        @Setter
        public static class MenuDetalle {

            private Integer id;
            private String nombre;
            private String estado;


}}
