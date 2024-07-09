package app.infrastructure.controller.reserva;

import app.application.services.reserva.ReservaService;
import app.domain.Reserva;
import app.infrastructure.item.reserva.dto.ReservaDetalleDto;
import app.infrastructure.item.reserva.dto.ReservaDto;
import app.infrastructure.item.reserva.dto.ReservaRequestDto;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/reserva")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }


    // Tabla reserva
    @GetMapping("/obtener")
    public Flux<ReservaDto> getAllReservas() {
        return reservaService.getAllReservas();
    }

    @GetMapping("/obtener/{id}")
    public Mono<ReservaDto> getReservaById(@PathVariable Integer id) {
        return reservaService.getReservaById(id);
    }

    @GetMapping("/obtener/estado/{estado}")
    public Flux<ReservaDto> getReservasByEstado(@PathVariable String estado) {
        return reservaService.getReservasByEstado(estado);
    }

    @PutMapping("/editar/{id}")
    public Mono<ReservaDto> editarReserva(@PathVariable Integer id, @RequestBody ReservaDto reservaDto) {
        return reservaService.editarReserva(id, reservaDto);
    }

    @PutMapping("/desactivar/{id}")
    public Mono<ReservaDto> desactivarReserva(@PathVariable Integer id) {
        return reservaService.desactivarReserva(id);
    }

    @PutMapping("/restaurar/{id}")
    public Mono<ReservaDto> restaurarReserva(@PathVariable Integer id) {
        return reservaService.restaurarReserva(id);
    }

    @GetMapping("/obtener/situacion/{situacion}")
    public Flux<ReservaDto> getReservasBySituacion(@PathVariable String situacion) {
        return reservaService.getReservasBySituacion(situacion);
    }

    // Tabla reserva detalle
    @GetMapping("/reserva-detalle/obtener")
    public Flux<ReservaDetalleDto> getAllReservasDetalles() {
        return reservaService.getAllReservasDetalles();
    }


    @GetMapping("/reserva-detalle/obtener/{id}")
    public Mono<ReservaDetalleDto> getReservaDetalleById(@PathVariable Integer id) {
        return reservaService.getReservaDetalleById(id);
    }

    @PutMapping("/reserva-detalle/editar/{id}")
    public Mono<ReservaDetalleDto> editarReservaDetalle(@PathVariable Integer id, @RequestBody ReservaDetalleDto reservaDetalleDto) {
        return reservaService.editarReservaDetalle(id, reservaDetalleDto);
    }

    @PutMapping("/reserva-detalle/desactivar/{id}")
    public Mono<ReservaDetalleDto> desactivarReservaDetalle(@PathVariable Integer id) {
        return reservaService.desactivarReservaDetalle(id);
    }

    @PutMapping("/reserva-detalle/restaurar/{id}")
    public Mono<ReservaDetalleDto> restaurarReservaDetalle(@PathVariable Integer id) {
        return reservaService.restaurarReservaDetalle(id);
    }

    // Reserva anidada

    @GetMapping("/transaccional-reserva/obtener")
    public Flux<Reserva> getAllReservasWithDetails() {
        return reservaService.getAllReservasWithDetails();
    }

    @GetMapping("/transaccional-reserva/obtener/{id}")
    public Mono<Reserva> getReservaWithDetails(@PathVariable Integer id) {
        return reservaService.getReservaWithDetails(id);
    }

    @GetMapping("/transaccional-reserva/obtener/estado/{estado}")
    public Flux<Reserva> getReservasWithDetailsByEstado(@PathVariable String estado) {
        return reservaService.getReservasWithDetailsByEstado(estado);
    }

    @PostMapping("/transaccional-reserva/crear")
    public Mono<ReservaDto> crearReserva(@RequestBody ReservaRequestDto reservaRequestDto) {
        return reservaService.crearReserva(reservaRequestDto);
    }

    @PostMapping("/transaccional-reserva/anular/{id}")
    public Mono<ReservaDto> anularReserva(@PathVariable Integer id) {
        return reservaService.anularReserva(id);
    }

    @PostMapping("/transaccional-reserva/confirmar/{id}")
    public Mono<ReservaDto> confirmarReserva(@PathVariable Integer id) {
        return reservaService.confirmarReserva(id);
    }
}