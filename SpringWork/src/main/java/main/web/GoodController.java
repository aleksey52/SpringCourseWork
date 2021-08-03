package main.web;

import main.entity.Good;
import main.exception.GoodNotFoundException;
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
@RequestMapping("/wc/good")
public class GoodController {

    private GoodService goodService;
    private SaleService saleService;
    private WarehouseService warehouseService;

    @Autowired
    public GoodController(GoodService goodService, SaleService saleService, WarehouseService warehouseService) {
        this.goodService = goodService;
        this.saleService = saleService;
        this.warehouseService = warehouseService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Good>> getAllGoods() {
        List<Good> list = goodService.listGoods();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Good> getGood(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(goodService.findGood(id), HttpStatus.OK);
        } catch(GoodNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Good not found");
        }
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<Good> getGoodByName(@PathVariable("name") String name) {
        try {
            return new ResponseEntity<>(goodService.findGoodByName(name), HttpStatus.OK);
        } catch(GoodNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Good not found");
        }
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public Good addGood(@RequestBody Good good) {
        return goodService.addGood(good);
    }

    @PostMapping("/delete/{id}")
    public void deleteGood(@PathVariable Long id) {
        Good good = goodService.findGood(id);
        try {
            saleService.findSaleByGood(good);
        } catch (Exception exceptionSale) {
            if (warehouseService.findInOneByGood(good).isEmpty() && warehouseService.findInTwoByGood(good).isEmpty()) {
                goodService.deleteGood(id);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Good is present in SALES or in WAREHOUSES");
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Good is present in SALES or in WAREHOUSES");
    }
}
