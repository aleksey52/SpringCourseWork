package main.service;

import main.entity.Good;
import main.entity.WarehouseOne;
import main.entity.WarehouseTwo;
import main.exception.GoodNotFoundException;
import main.repository.WarehouseOneRepository;
import main.repository.WarehouseTwoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    @Autowired
    private WarehouseOneRepository warehouseOneRepository;

    @Autowired
    private WarehouseTwoRepository warehouseTwoRepository;

    @Override
    public List<WarehouseOne> listWarehouseOne() {
        return (List<WarehouseOne>) warehouseOneRepository.findAll();
    }

    @Override
    public List<WarehouseTwo> listWarehouseTwo() {
        return (List<WarehouseTwo>) warehouseTwoRepository.findAll();
    }


    @Override
    public WarehouseOne findInOneById(Long id) {
        Optional<WarehouseOne> warehouseOneOptional = warehouseOneRepository.findById(id);
        if (warehouseOneOptional.isPresent()) {
            return warehouseOneOptional.get();
        } else {
            throw new GoodNotFoundException("The entry with the specified id is not found");
        }
    }

    @Override
    public WarehouseTwo findInTwoById(Long id) {
        Optional<WarehouseTwo> warehouseTwoOptional = warehouseTwoRepository.findById(id);
        if (warehouseTwoOptional.isPresent()) {
            return warehouseTwoOptional.get();
        } else {
            throw new GoodNotFoundException("The entry with the specified good is not found");
        }
    }

    @Override
    public List<WarehouseOne> findInOneByGood(Good good) {
        Optional<List<WarehouseOne>> warehouseOneOptional = warehouseOneRepository.findByGood(good);
        if (warehouseOneOptional.isPresent()) {
            return warehouseOneOptional.get();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<WarehouseTwo> findInTwoByGood(Good good) {
        Optional<List<WarehouseTwo>> warehouseTwoOptional = warehouseTwoRepository.findByGood(good);
        if (warehouseTwoOptional.isPresent()) {
            return warehouseTwoOptional.get();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public WarehouseOne addToOne(WarehouseOne warehouseOne) {
        return warehouseOneRepository.save(warehouseOne);
    }

    @Override
    public WarehouseTwo addToTwo(WarehouseTwo warehouseTwo) {
        return warehouseTwoRepository.save(warehouseTwo);
    }

    @Override
    public void deleteInOne(Long id) {
        warehouseOneRepository.deleteById(id);
    }

    @Override
    public void deleteInTwo(Long id) {
        warehouseTwoRepository.deleteById(id);
    }

    @Override
    public Long sizeOne() {
        return warehouseOneRepository.count();
    }

    @Override
    public Long sizeTwo() {
        return warehouseTwoRepository.count();
    }
}
