package app.infrastructure.item.reserva.repository;

import app.infrastructure.item.reserva.dto.ReservaDto;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ReservaRepository extends ReactiveCrudRepository<ReservaDto, Integer> {


    Flux<ReservaDto> findAllByEstado(String estado);

    Flux<ReservaDto> findAllBySituacion(String situacion);
}
