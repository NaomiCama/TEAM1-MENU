package app.infrastructure.item.reserva.repository;

import app.infrastructure.item.reserva.dto.ReservaDetalleDto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface ReservaDetalleRepository extends ReactiveCrudRepository<ReservaDetalleDto, Integer> {
    @Query("SELECT * FROM RESERVADETALLE WHERE id_reserva = :id_reserva")
    Flux<ReservaDetalleDto> findAllById_reserva(Integer id_reserva);
}