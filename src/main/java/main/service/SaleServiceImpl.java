package main.service;

import main.entity.Good;
import main.entity.Sale;
import main.exception.SaleNotFoundException;
import main.repository.SaleRepository;
import main.repository.WarehouseOneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleServiceImpl implements SaleService {
    @Autowired
    private SaleRepository saleRepository;

    @Override
    public List<Sale> listSales() {
        return (List<Sale>) saleRepository.findAll();
    }

    @Override
    public Sale findSale(Long id) {
        Optional<Sale> saleOptional = saleRepository.findById(id);
        if (saleOptional.isPresent()) {
            return saleOptional.get();
        } else {
            throw new SaleNotFoundException("Sale not found");
        }
    }

    @Override
    public List<Sale> findSaleByGood(Good good) {
        Optional<List<Sale>> saleOptional = saleRepository.findByGood(good);
        if (saleOptional.isPresent()) {
            return saleOptional.get();
        } else {
            throw new SaleNotFoundException("Sales not found");
        }
    }

    @Override
    public Sale addSale(Sale sale) {
        return saleRepository.save(sale);
    }

    @Override
    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }
}
