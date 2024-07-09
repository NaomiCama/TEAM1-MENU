package app.infrastructure.controller.menu;

import app.application.services.menu.MenuService;
import app.infrastructure.item.comida.dto.SeleccionDto;
import app.infrastructure.item.menu.dto.MenuDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/obtener")
    public Flux<MenuDto> getAllMenu() {
        return menuService.getAllMenu();
    }

    @GetMapping("/obtener/{estado}")
    public Flux<MenuDto> getMenuByEstado(@PathVariable String estado) {
        return menuService.getMenuByEstado(estado);
    }

    @GetMapping("/{id}")
    public Mono<MenuDto> getMenuById(@PathVariable Long id) {
        return menuService.getMenuById(id);
    }

    @PostMapping("/insertar")
    public Mono<MenuDto> insertMenu(@RequestBody MenuDto menuDto) {
        return menuService.insertMenu(menuDto);
    }

    @PutMapping("/eliminar/{id}")
    public Mono<MenuDto> eliminarMenuById(@PathVariable Long id) {
        return menuService.eliminarMenuById(id);
    }

    @PutMapping("/restaurar/{id}")
    public Mono<MenuDto> restaurarMenuById(@PathVariable Long id) {
        return menuService.restaurarMenuById(id);
    }

    @PutMapping("/editar/{id}")
    public Mono<MenuDto> updateMenuById(@RequestBody MenuDto menuDto, @PathVariable Long id) {
        return menuService.updateMenuById(menuDto, id);
    }

    // En MenuController.java

    @GetMapping("/seleccion")
    public Flux<SeleccionDto> getSeleccionMenu() {
        return menuService.getSeleccionMenu();
    }

// En MenuController.java

    @GetMapping("/seleccion/{nombremenu}")
    public Flux<SeleccionDto> getSeleccionMenuPorNombre(@PathVariable String nombremenu) {
        return menuService.getSeleccionMenuPorNombre(nombremenu);
    }


    @GetMapping("/seleccion/id/{menuid}")
    public Flux<SeleccionDto> getSeleccionMenuPorId(@PathVariable Long menuid) {
        return menuService.getSeleccionMenuPorId(menuid);
    }

}