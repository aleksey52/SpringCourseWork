package main.repository;

import main.entity.Good;
import main.entity.WarehouseOne;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseOneRepository extends CrudRepository<WarehouseOne, Long> {
    Optional<List<WarehouseOne>> findByGood(Good good);
}
