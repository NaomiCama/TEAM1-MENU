package app.infrastructure.controller.platomenu;

import app.application.services.platomenu.PlatoMenuService;
import app.domain.PlatoMenu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/platomenu")
public class PlatoMenuController {
    private final PlatoMenuService platoMenuService;

    public PlatoMenuController(PlatoMenuService platoMenuService) {
        this.platoMenuService = platoMenuService;
    }

    @GetMapping("/obtener" )
    public Flux<PlatoMenu> getAllPlatoMenus() {
        return platoMenuService.getAllPlatoMenus();
    }
}