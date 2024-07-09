package app.infrastructure.controller.comida;

import app.application.services.comida.ComidaService;
import app.infrastructure.item.comida.dto.ComidaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/comida")
public class ComidaController {

    @Autowired
    private ComidaService comidaService;

    @GetMapping("/obtener")
    public Flux<ComidaDto> getAllComida() {
        return comidaService.getAllComida();
    }

    @GetMapping("/obtener/{estado}")
    public Flux<ComidaDto> getComidaByEstado(@PathVariable String estado) {
        return comidaService.getComidaByEstado(estado);
    }

    @GetMapping("/{id}")
    public Mono<ComidaDto> getComidaById(@PathVariable Long id) {
        return comidaService.getComidaById(id);
    }

    @PostMapping("/crear")
    public Mono<ComidaDto> insertComida(@RequestBody ComidaDto comidaDto) {
        return comidaService.insertComida(comidaDto);
    }

    @PutMapping("/eliminar/{id}")
    public Mono<ComidaDto> eliminarComidaById(@PathVariable Long id) {
        return comidaService.eliminarComidaById(id);
    }

    @PutMapping("/restaurar/{id}")
    public Mono<ComidaDto> restaurarComidaById(@PathVariable Long id) {
        return comidaService.restaurarComidaById(id);
    }

    @PutMapping("/editar/{id}")
    public Mono<ComidaDto> updateComidaById(@RequestBody ComidaDto comida, @PathVariable Long id) {
        return comidaService.updateComidaById(comida, id);
    }

    @GetMapping("/obtener/nombre/{nombrec}")
    public Mono<ComidaDto> getComidaByNombre(@PathVariable String nombrec) {
        return comidaService.getComidaByNombre(nombrec);
    }

}