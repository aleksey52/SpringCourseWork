package main.web;

import main.entity.Good;
import main.entity.Sale;
import main.entity.WarehouseOne;
import main.entity.WarehouseTwo;
import main.exception.SaleNotFoundException;
import main.service.GoodService;
import main.service.SaleService;
import main.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/wc/sale")
public class SaleController {

    private SaleService saleService;
    private GoodService goodService;
    private WarehouseService warehouseService;

    @Autowired
    public SaleController(GoodService goodService, SaleService saleService, WarehouseService warehouseService) {
        this.goodService = goodService;
        this.saleService = saleService;
        this.warehouseService = warehouseService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Sale>> getAllSales() {
        return new ResponseEntity<>(saleService.listSales(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSale(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(saleService.findSale(id), HttpStatus.OK);
        } catch(SaleNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sale not found");
        }
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<Sale>> getSaleByName(@PathVariable("name") String name) {
        try {
            Good good = goodService.findGoodByName(name);
            return new ResponseEntity<>(saleService.findSaleByGood(good), HttpStatus.OK);
        } catch(SaleNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sales not found");
        }
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public Sale addSale(@RequestBody Sale sale) {
        List<WarehouseOne> list1 = warehouseService.findInOneByGood(sale.getGood());
        List<WarehouseTwo> list2 = warehouseService.findInTwoByGood(sale.getGood());
        int goodCount1 = 0;
        int goodCount2 = 0;

        for (WarehouseOne elem : list1) {
            goodCount1 += elem.getGood_count();
        }

        for (WarehouseTwo elem : list2) {
            goodCount2 += elem.getGood_count();
        }

        if (goodCount1 + goodCount2 < sale.getGood_count()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "There is no such quantity of goods. AVAILABLE QUANTITY: " + goodCount1 + goodCount2);
        }

        int getCount = 0;
        int i = 0;
        int k = 0;
        Long id;

        while (sale.getGood_count() > getCount) {
            if (getCount < goodCount1) {
                id = list1.get(i).getId();
                warehouseService.deleteInOne(id);
                getCount += list1.get(i).getGood_count();
                i++;
            } else {
                id = list2.get(k).getId();
                warehouseService.deleteInTwo(id);
                getCount += list2.get(k).getGood_count();
                k++;
            }
        }

        if (sale.getGood_count() < getCount) {
            if (k == 0) {
                warehouseService.addToOne(new WarehouseOne(sale.getGood(), getCount-sale.getGood_count()));
            } else {
                warehouseService.addToTwo(new WarehouseTwo(sale.getGood(), getCount-sale.getGood_count()));
            }
        }

        return saleService.addSale(sale);
    }

    @PostMapping("/delete/{id}")
    public void deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
    }
}
