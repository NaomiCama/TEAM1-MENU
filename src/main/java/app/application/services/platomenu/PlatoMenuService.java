package app.application.services.platomenu;

import app.domain.PlatoMenu;
import app.infrastructure.item.comida.dto.ComidaDto;
import app.infrastructure.item.menu.dto.MenuDto;
import app.infrastructure.item.comida.repository.ComidaRepository;
import app.infrastructure.item.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlatoMenuService {
    private final ComidaRepository comidaRepository;
    private final MenuRepository menuRepository;

    public PlatoMenuService(ComidaRepository comidaRepository, MenuRepository menuRepository) {
        this.comidaRepository = comidaRepository;
        this.menuRepository = menuRepository;
    }

    public Mono<PlatoMenu> constructPlatoMenu(ComidaDto comidas) {
        return Mono.zip(
                        Mono.just(comidas),
                        menuRepository.findById(comidas.getMenuid())
                )
                .map(tuple -> {
                    ComidaDto comidasDto = tuple.getT1();
                    MenuDto menuDto = tuple.getT2();

                    PlatoMenu.MenuDetalle menuDetalle = new PlatoMenu.MenuDetalle();
                    menuDetalle.setId(menuDto.getMenuid().intValue());
                    menuDetalle.setNombre(menuDto.getNombrem());
                    menuDetalle.setEstado(menuDto.getEstado());

                    PlatoMenu platoMenu = new PlatoMenu();
                    platoMenu.setId(comidasDto.getComidaid().intValue());
                    platoMenu.setNombre(comidasDto.getNombrec());
                    platoMenu.setCategoria(comidasDto.getCategoria());
                    platoMenu.setPrecio(comidasDto.getPrecio());
                    platoMenu.setStock(comidasDto.getStock());
                    platoMenu.setImage(comidasDto.getImage());
                    platoMenu.setEstado(comidasDto.getEstado());
                    platoMenu.setMenu_detalle(menuDetalle);

                    return platoMenu;
                });
    }

    public Flux<PlatoMenu> getAllPlatoMenus() {
        return comidaRepository.findAll()
                .flatMap(this::constructPlatoMenu);
    }
}