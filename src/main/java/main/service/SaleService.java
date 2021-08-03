package main.service;

import main.entity.Good;
import main.entity.Sale;

import java.util.List;

public interface SaleService {
    List<Sale> listSales();
    Sale findSale(Long id);
    List<Sale> findSaleByGood(Good good);
    Sale addSale(Sale sale);
    void deleteSale(Long id);
}
