package app.infrastructure.item.menu.repository;

import app.infrastructure.item.menu.dto.MenuDto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MenuRepository extends ReactiveCrudRepository<MenuDto, Long> {

    @Query("SELECT * FROM Menus WHERE estado = :estado")
    Flux<MenuDto> findByEstado(@Param("estado") String estado);

    Mono<MenuDto> findByMenuid(Long menuid);

    Flux<MenuDto> findByNombrem(String nombrem);

}

