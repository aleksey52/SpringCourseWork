package main.repository;

import main.entity.Good;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GoodRepository extends CrudRepository<Good, Long> {
    Optional<Good> findByName(String name);
}
