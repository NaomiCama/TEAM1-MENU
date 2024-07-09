package app.infrastructure.item.comida.repository;


import app.infrastructure.item.comida.dto.ComidaDto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface ComidaRepository extends ReactiveCrudRepository<ComidaDto, Long> {


    @Query("SELECT * FROM Comidas WHERE estado = :estado")
    Flux<ComidaDto> findByEstado(@Param("estado") String estado);

    Flux<ComidaDto> findByMenuid(Long menuid);

    Mono<ComidaDto> findByNombrec(String nombrec);


}