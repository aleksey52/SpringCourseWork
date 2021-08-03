package main.repository;

import main.entity.Good;
import main.entity.Sale;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SaleRepository extends CrudRepository<Sale, Long> {
    Optional<List<Sale>> findByGood(Good good);
}
