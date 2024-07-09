package app.application.services.reserva;

import app.application.services.comida.ComidaService;
import app.application.services.mailtrap.EmailService;
import app.domain.Reserva;
import app.infrastructure.item.reserva.dto.PlatoMenuKafkaDto;
import app.infrastructure.item.reserva.dto.ReservaDetalleDto;
import app.infrastructure.item.reserva.dto.ReservaDto;
import app.infrastructure.item.reserva.dto.ReservaRequestDto;
import app.infrastructure.item.reserva.repository.ReservaDetalleRepository;
import app.infrastructure.item.reserva.repository.ReservaRepository;
import app.infrastructure.kafka.producer.KafkaProducerService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaDetalleRepository reservaDetalleRepository;

    private final EmailService emailService;

    private final ComidaService comidaService;


    public ReservaService(ReservaRepository reservaRepository, ReservaDetalleRepository reservaDetalleRepository, EmailService emailService, ComidaService comidaService, KafkaProducerService kafkaProducerService) {
        this.reservaRepository = reservaRepository;
        this.reservaDetalleRepository = reservaDetalleRepository;
        this.emailService = emailService;
        this.comidaService = comidaService;
        this.kafkaProducerService = kafkaProducerService;
    }

    public Flux<ReservaDto> getAllReservas() {
        return reservaRepository.findAll();
    }

    public Flux<ReservaDetalleDto> getAllReservasDetalles() {
        return reservaDetalleRepository.findAll();
    }

    public Mono<ReservaDto> getReservaById(Integer id) {
        return reservaRepository.findById(id);
    }

    public Mono<ReservaDetalleDto> getReservaDetalleById(Integer id) {
        return reservaDetalleRepository.findById(id);
    }

    public Mono<Reserva> getReservaWithDetails(Integer id) {
        Mono<ReservaDto> reservaDtoMono = reservaRepository.findById(id);
        Flux<ReservaDetalleDto> reservaDetalleDtoFlux = reservaDetalleRepository.findAllById_reserva(id);

        return reservaDtoMono.zipWith(reservaDetalleDtoFlux.collectList(), this::convertToReserva);
    }

    public Flux<Reserva> getAllReservasWithDetails() {
        Flux<ReservaDto> reservaDtoFlux = reservaRepository.findAll();

        return reservaDtoFlux.flatMap(reservaDto -> {
            Flux<ReservaDetalleDto> reservaDetalleDtoFlux = reservaDetalleRepository.findAllById_reserva(reservaDto.getId_reserva());

            return reservaDetalleDtoFlux.collectList().map(reservaDetalleDtos -> convertToReserva(reservaDto, reservaDetalleDtos));
        });
    }

    public Mono<ReservaDto> desactivarReserva(Integer id) {
        return reservaRepository.findById(id)
                .flatMap(reserva -> {
                    reserva.setEstado("I");
                    return reservaRepository.save(reserva);
                });
    }

    public Mono<ReservaDto> restaurarReserva(Integer id) {
        return reservaRepository.findById(id)
                .flatMap(reserva -> {
                    reserva.setEstado("A");
                    return reservaRepository.save(reserva);
                });
    }

    public Mono<ReservaDto> editarReserva(Integer id, ReservaDto reservaDto) {
        return reservaRepository.findById(id)
                .flatMap(existingReserva -> {
                    existingReserva.setNombre(reservaDto.getNombre());
                    existingReserva.setCorreo(reservaDto.getCorreo());
                    existingReserva.setHora_reserva(reservaDto.getHora_reserva());
                    existingReserva.setNum_personas(reservaDto.getNum_personas());
                    existingReserva.setSituacion(reservaDto.getSituacion());
                    existingReserva.setMonto_total(reservaDto.getMonto_total());
                    existingReserva.setEstado(reservaDto.getEstado());
                    return reservaRepository.save(existingReserva);
                });
    }

    public Mono<ReservaDetalleDto> editarReservaDetalle(Integer id, ReservaDetalleDto reservaDetalleDto) {
        return reservaDetalleRepository.findById(id)
                .flatMap(existingReservaDetalle -> {
                    existingReservaDetalle.setId_reserva(reservaDetalleDto.getId_reserva());
                    existingReservaDetalle.setComidaid(reservaDetalleDto.getComidaid());
                    existingReservaDetalle.setNombrepedido(reservaDetalleDto.getNombrepedido());
                    existingReservaDetalle.setCantidad(reservaDetalleDto.getCantidad());
                    existingReservaDetalle.setEstado(reservaDetalleDto.getEstado());
                    return reservaDetalleRepository.save(existingReservaDetalle);
                });
    }

    public Flux<ReservaDto> getReservasBySituacion(String situacion) {
        return reservaRepository.findAllBySituacion(situacion);
    }


    public Flux<ReservaDto> getReservasByEstado(String estado) {
        return reservaRepository.findAllByEstado(estado);
    }

    public Flux<Reserva> getReservasWithDetailsByEstado(String estado) {
        Flux<ReservaDto> reservaDtoFlux = reservaRepository.findAllByEstado(estado);

        return reservaDtoFlux.flatMap(reservaDto -> {
            Flux<ReservaDetalleDto> reservaDetalleDtoFlux = reservaDetalleRepository.findAllById_reserva(reservaDto.getId_reserva());

            return reservaDetalleDtoFlux.collectList().map(reservaDetalleDtos -> convertToReserva(reservaDto, reservaDetalleDtos));
        });
    }

    public Mono<ReservaDetalleDto> desactivarReservaDetalle(Integer id) {
        return reservaDetalleRepository.findById(id)
                .flatMap(reservaDetalle -> {
                    reservaDetalle.setEstado("I");
                    return reservaDetalleRepository.save(reservaDetalle);
                });
    }

    public Mono<ReservaDetalleDto> restaurarReservaDetalle(Integer id) {
        return reservaDetalleRepository.findById(id)
                .flatMap(reservaDetalle -> {
                    reservaDetalle.setEstado("A");
                    return reservaDetalleRepository.save(reservaDetalle);
                });
    }

    public Mono<ReservaDto> crearReserva(ReservaRequestDto reservaRequestDto) {
        ReservaDto reservaDto = new ReservaDto();
        reservaDto.setNombre(reservaRequestDto.getNombre());
        reservaDto.setNum_personas(reservaRequestDto.getNum_personas());
        reservaDto.setCorreo(reservaRequestDto.getCorreo());
        reservaDto.setHora_reserva(reservaRequestDto.getHora_reserva());
        reservaDto.setSituacion(reservaRequestDto.getSituacion());
        reservaDto.setMonto_total(reservaRequestDto.getMonto_total());
        reservaDto.setEstado(reservaRequestDto.getEstado());

        return reservaRepository.save(reservaDto)
                .flatMap(savedReserva -> {
                    List<ReservaDetalleDto> detalles = reservaRequestDto.getReserva_detalle();
                    detalles.forEach(detalle -> detalle.setId_reserva(savedReserva.getId_reserva()));
                    return Flux.fromIterable(detalles)
                            .flatMap(reservaDetalleRepository::save)
                            .then(Mono.just(savedReserva));
                })
                .doOnSuccess(reserva -> {
                    String subject = "Confirmación de reserva";
                    String text = String.format("Su reserva ha sido creada con éxito. Gracias por preferirnos.\n\n" +
                                    "Detalles de la reserva:\n" +
                                    "ID Reserva: %d\n" +
                                    "Nombre: %s\n" +
                                    "Correo: %s\n" +
                                    "Hora de Reserva: %s\n" +
                                    "Número de Personas: %d\n" +
                                    "Situación: %s\n" +
                                    "Monto Total: %.2f\n" +
                                    "Estado: %s\n",
                            reserva.getId_reserva(),
                            reserva.getNombre(),
                            reserva.getCorreo(),
                            reserva.getHora_reserva(),
                            reserva.getNum_personas(),
                            reserva.getSituacion(),
                            reserva.getMonto_total(),
                            reserva.getEstado());
                    emailService.sendSimpleMessage(reservaRequestDto.getCorreo(), subject, text);
                });
    }


    private final KafkaProducerService kafkaProducerService;

    public Mono<ReservaDto> confirmarReserva(Integer id) {
        return reservaRepository.findById(id)
                .flatMap(existingReserva -> {
                    existingReserva.setSituacion("C");
                    return reservaRepository.save(existingReserva);
                })
                .flatMap(reserva -> {
                    return reservaDetalleRepository.findAllById_reserva(reserva.getId_reserva())
                            .flatMap(reservaDetalle -> {
                                return comidaService.getComidaById(Long.valueOf(reservaDetalle.getComidaid()))
                                        .flatMap(comidaDto -> {
                                            comidaDto.setStock(comidaDto.getStock() - reservaDetalle.getCantidad());
                                            PlatoMenuKafkaDto platoMenuKafkaDto = new PlatoMenuKafkaDto();
                                            platoMenuKafkaDto.setId(comidaDto.getMenuid().intValue());
                                            platoMenuKafkaDto.setStock(comidaDto.getStock());
                                            kafkaProducerService.sendPlatoMenuToTopic(platoMenuKafkaDto);
                                            return comidaService.updateComidaById(comidaDto, Long.valueOf(reservaDetalle.getComidaid()));
                                        });
                            })
                            .then(Mono.just(reserva));
                });
    }

    public Mono<ReservaDto> anularReserva(Integer id) {
        return reservaRepository.findById(id)
                .flatMap(existingReserva -> {
                    existingReserva.setSituacion("A");
                    return reservaRepository.save(existingReserva);
                })
                .flatMap(reserva -> {
                    return reservaDetalleRepository.findAllById_reserva(reserva.getId_reserva())
                            .flatMap(reservaDetalle -> {
                                return comidaService.getComidaById(Long.valueOf(reservaDetalle.getComidaid()))
                                        .flatMap(comidaDto -> {
                                            comidaDto.setStock(comidaDto.getStock() + reservaDetalle.getCantidad());
                                            PlatoMenuKafkaDto platoMenuKafkaDto = new PlatoMenuKafkaDto();
                                            platoMenuKafkaDto.setId(comidaDto.getMenuid().intValue());
                                            platoMenuKafkaDto.setStock(comidaDto.getStock());
                                            kafkaProducerService.sendPlatoMenuToTopic(platoMenuKafkaDto);
                                            return comidaService.updateComidaById(comidaDto, Long.valueOf(reservaDetalle.getComidaid()));
                                        });
                            })
                            .then(Mono.just(reserva));
                });
    }




    private Reserva convertToReserva(ReservaDto reservaDto, List<ReservaDetalleDto> reservaDetalleDtos) {
        Reserva reserva = new Reserva();
        reserva.setId_reserva(reservaDto.getId_reserva());
        reserva.setNombre(reservaDto.getNombre());
        reserva.setCorreo(reservaDto.getCorreo());
        reserva.setHora_reserva(reservaDto.getHora_reserva());
        reserva.setNum_personas(reservaDto.getNum_personas());
        reserva.setSituacion(reservaDto.getSituacion());
        reserva.setMonto_total(reservaDto.getMonto_total());
        reserva.setEstado(reservaDto.getEstado());

        List<Reserva.ReservaDetalle> reservaDetalles = reservaDetalleDtos.stream()
                .map(reservaDetalleDto -> {
                    Reserva.ReservaDetalle reservaDetalle = new Reserva.ReservaDetalle();
                    reservaDetalle.setId(reservaDetalleDto.getId());
                    reservaDetalle.setIdReserva(reservaDetalleDto.getId_reserva());
                    reservaDetalle.setComidaid(reservaDetalleDto.getComidaid());
                    reservaDetalle.setNombrePedido(reservaDetalleDto.getNombrepedido());
                    reservaDetalle.setCantidad(reservaDetalleDto.getCantidad());
                    reservaDetalle.setEstado(reservaDetalleDto.getEstado());
                    return reservaDetalle;
                })
                .collect(Collectors.toList());

        reserva.setReserva_detalle(reservaDetalles);

        return reserva;
    }
}