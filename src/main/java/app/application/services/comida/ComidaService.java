package app.application.services.comida;

import app.application.services.platomenu.PlatoMenuService;
import app.infrastructure.item.comida.dto.ComidaDto;
import app.infrastructure.item.comida.repository.ComidaRepository;
import app.infrastructure.kafka.producer.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ComidaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComidaService.class);

    @Autowired
    private ComidaRepository comidaRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private PlatoMenuService platoMenuService;

    public Flux<ComidaDto> getAllComida() {
        return comidaRepository.findAll();
    }
    public Flux<ComidaDto> getComidaByEstado(String estado) {
        return comidaRepository.findByEstado(estado);
    }

    public Mono<ComidaDto> getComidaById(Long id) {
        return comidaRepository.findById(id);
    }

    public Mono<ComidaDto> insertComida(ComidaDto comidaDto) {
        return comidaRepository.save(comidaDto)
                .doOnSuccess(c -> {
                    platoMenuService.constructPlatoMenu(c)
                            .subscribe(platoMenu -> kafkaProducerService.sendPlatoMenuToTopic(platoMenu));
                });
    }

    public Mono<ComidaDto> eliminarComidaById(Long id) {
        return comidaRepository.findById(id)
                .flatMap(c -> {
                    c.setEstado("I");
                    LOGGER.info("Eliminando lógicamente comida con id: {}", id);
                    return comidaRepository.save(c);
                })
                .doOnSuccess(c -> {
                    platoMenuService.constructPlatoMenu(c)
                            .subscribe(platoMenu -> kafkaProducerService.sendPlatoMenuToEditTopic(platoMenu));
                });
    }

    public Mono<ComidaDto> restaurarComidaById(Long id) {
        return comidaRepository.findById(id)
                .flatMap(c -> {
                    c.setEstado("A");
                    LOGGER.info("Restaurando lógicamente comida con id: {}", id);
                    return comidaRepository.save(c);
                })
                .doOnSuccess(c -> {
                    platoMenuService.constructPlatoMenu(c)
                            .subscribe(platoMenu -> kafkaProducerService.sendPlatoMenuToEditTopic(platoMenu));
                });
    }

    public Mono<ComidaDto> updateComidaById(ComidaDto comida, Long id) {
        return comidaRepository.findById(id)
                .flatMap(c -> {
                    c.setNombrec(comida.getNombrec());
                    c.setCategoria(comida.getCategoria());
                    c.setPrecio(comida.getPrecio());
                    c.setEstado(comida.getEstado());
                    c.setStock(comida.getStock());
                    c.setImage(comida.getImage());
                    c.setMenuid(comida.getMenuid()); // You may need to handle menuid update
                    return comidaRepository.save(c);
                })
                .doOnSuccess(c -> {
                    platoMenuService.constructPlatoMenu(c)
                            .subscribe(platoMenu -> kafkaProducerService.sendPlatoMenuToEditTopic(platoMenu));
                });
    }

    public Mono<ComidaDto> getComidaByNombre(String nombrec) {
        return comidaRepository.findByNombrec(nombrec);
    }

}