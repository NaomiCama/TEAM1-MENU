package app.application.services.menu;

import app.infrastructure.item.comida.dto.SeleccionDto;
import app.infrastructure.item.comida.repository.ComidaRepository;
import app.infrastructure.item.menu.dto.MenuDto;
import app.infrastructure.item.menu.repository.MenuRepository;
import app.infrastructure.kafka.producer.KafkaProducerService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final KafkaProducerService kafkaProducerService;
    private final ComidaRepository comidaRepository;

    public MenuService(MenuRepository menuRepository, KafkaProducerService kafkaProducerService, ComidaRepository comidaRepository) {
        this.menuRepository = menuRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.comidaRepository = comidaRepository;
    }

    public Flux<MenuDto> getAllMenu() {
        return menuRepository.findAll();
    }

    public Flux<MenuDto> getMenuByEstado(String estado) {
        return menuRepository.findByEstado(estado);
    }

    public Mono<MenuDto> getMenuById(Long id) {
        return menuRepository.findById(id);
    }

    public Mono<MenuDto> insertMenu(MenuDto menuDto) {
        return menuRepository.save(menuDto);
    }

    public Mono<MenuDto> eliminarMenuById(Long id) {
        return menuRepository.findById(id)
                .flatMap(m -> {
                    m.setEstado("I");
                    return menuRepository.save(m);
                })
                .doOnSuccess(m -> kafkaProducerService.sendMenuToPmmsMdtTopic(m));
    }

    public Mono<MenuDto> restaurarMenuById(Long id) {
        return menuRepository.findById(id)
                .flatMap(m -> {
                    m.setEstado("A");
                    return menuRepository.save(m);
                })
                .doOnSuccess(m -> kafkaProducerService.sendMenuToPmmsMdtTopic(m));
    }

    public Mono<MenuDto> updateMenuById(MenuDto menuDto, Long id) {
        return menuRepository.findById(id)
                .flatMap(m -> {
                    m.setNombrem(menuDto.getNombrem());
                    m.setEstado(menuDto.getEstado());
                    return menuRepository.save(m);
                })
                .doOnSuccess(m -> kafkaProducerService.sendMenuToPmmsMdtTopic(m));
    }

    // En MenuService.java

    public Flux<SeleccionDto> getSeleccionMenu() {
        return menuRepository.findAll()
                .flatMap(menu -> {
                    return comidaRepository.findByMenuid(menu.getMenuid())
                            .map(comida -> {
                                SeleccionDto seleccionDto = new SeleccionDto();
                                seleccionDto.setMenuid(menu.getMenuid());
                                seleccionDto.setNombremenu(menu.getNombrem());
                                seleccionDto.setNombrecomida(comida.getNombrec());
                                seleccionDto.setPrecio(comida.getPrecio());
                                seleccionDto.setCategoria(comida.getCategoria());
                                return seleccionDto;
                            });
                });
    }


    // En MenuService.java

    public Flux<SeleccionDto> getSeleccionMenuPorNombre(String nombremenu) {
        return menuRepository.findByNombrem(nombremenu)
                .flatMap(menu -> {
                    return comidaRepository.findByMenuid(menu.getMenuid())
                            .map(comida -> {
                                SeleccionDto seleccionDto = new SeleccionDto();
                                seleccionDto.setMenuid(menu.getMenuid());
                                seleccionDto.setNombremenu(menu.getNombrem());
                                seleccionDto.setNombrecomida(comida.getNombrec());
                                seleccionDto.setPrecio(comida.getPrecio());
                                seleccionDto.setCategoria(comida.getCategoria());
                                return seleccionDto;
                            });
                });
    }


    // En MenuService.java

// En MenuService.java

    public Flux<SeleccionDto> getSeleccionMenuPorId(Long menuid) {
        return menuRepository.findById(menuid)
                .flatMapMany(menu -> {
                    return comidaRepository.findByMenuid(menu.getMenuid())
                            .map(comida -> {
                                SeleccionDto seleccionDto = new SeleccionDto();
                                seleccionDto.setMenuid(menu.getMenuid());
                                seleccionDto.setNombremenu(menu.getNombrem());
                                seleccionDto.setNombrecomida(comida.getNombrec());
                                seleccionDto.setPrecio(comida.getPrecio());
                                seleccionDto.setCategoria(comida.getCategoria());
                                return seleccionDto;
                            });
                });
    }

}