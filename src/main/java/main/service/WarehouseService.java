package main.service;

import main.entity.Good;
import main.entity.WarehouseOne;
import main.entity.WarehouseTwo;

import java.util.List;

public interface WarehouseService {
    List<WarehouseOne> listWarehouseOne();
    List<WarehouseTwo> listWarehouseTwo();
    WarehouseOne findInOneById(Long id);
    WarehouseTwo findInTwoById(Long id);
    List<WarehouseOne> findInOneByGood(Good good);
    List<WarehouseTwo> findInTwoByGood(Good good);
    WarehouseOne addToOne(WarehouseOne warehouseOne);
    WarehouseTwo addToTwo(WarehouseTwo warehouseTwo);
    void deleteInOne(Long id);
    void deleteInTwo(Long id);
    Long sizeOne();
    Long sizeTwo();
}
