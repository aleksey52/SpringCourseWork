package main.repository;

import main.entity.Good;
import main.entity.WarehouseTwo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface WarehouseTwoRepository extends CrudRepository<WarehouseTwo, Long> {
    Optional<List<WarehouseTwo>> findByGood(Good good);
}
