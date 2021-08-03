package main.web;

import main.entity.Good;
import main.entity.WarehouseOne;
import main.entity.WarehouseTwo;
import main.exception.GoodNotFoundException;
import main.service.GoodService;
import main.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/wc/warehouses")
public class WarehouseController {

    private WarehouseService warehouseService;
    private GoodService goodService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService, GoodService goodService) {
        this.warehouseService = warehouseService;
        this.goodService = goodService;
    }

    @GetMapping("/getAll/1")
    public ResponseEntity<List<WarehouseOne>> getAllOne() {
        return new ResponseEntity<>(warehouseService.listWarehouseOne(), HttpStatus.OK);
    }

    @GetMapping("/getAll/2")
    public ResponseEntity<List<WarehouseTwo>> getAllTwo() {
        return new ResponseEntity<>(warehouseService.listWarehouseTwo(), HttpStatus.OK);
    }

    @GetMapping("/getByName/1/{name}")
    public ResponseEntity<List<WarehouseOne>> getInOneByName(@PathVariable("name") String name) {
        try {
            Good good = goodService.findGoodByName(name);
            return new ResponseEntity<>(warehouseService.findInOneByGood(good), HttpStatus.OK);
        } catch(GoodNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found");
        }
    }

    @GetMapping("/getByName/2/{name}")
    public ResponseEntity<List<WarehouseTwo>> getInTwoByName(@PathVariable("name") String name) {
        try {
            Good good = goodService.findGoodByName(name);
            return new ResponseEntity<>(warehouseService.findInTwoByGood(good), HttpStatus.OK);
        } catch(GoodNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found");
        }
    }

    @PostMapping(value = "/add/1", consumes = "application/json", produces = "application/json")
    public WarehouseOne addWarehouseOne(@RequestBody WarehouseOne warehouseOne) {
        int maxSizeOne = 1000;
        if (warehouseService.sizeOne() + warehouseOne.getGood_count() <= maxSizeOne) {
            return warehouseService.addToOne(warehouseOne);
        }
        long freeSpace = maxSizeOne - warehouseService.sizeOne();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not enough space in the warehouse. FREE SPACE: " + freeSpace);
    }

    @PostMapping(value = "/add/2", consumes = "application/json", produces = "application/json")
    public WarehouseTwo addWarehouseTwo(@RequestBody WarehouseTwo warehouseTwo) {
        int maxSizeTwo = 1500;
        if (warehouseService.sizeTwo() + warehouseTwo.getGood_count() <= maxSizeTwo) {
            return warehouseService.addToTwo(warehouseTwo);
        }
        long freeSpace = maxSizeTwo - warehouseService.sizeTwo();

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not enough space in the warehouse. FREE SPACE: " + freeSpace);
    }

    @PostMapping("/delete/1/{id}")
    public void deleteWarehouseOne(@PathVariable Long id) {
        warehouseService.deleteInOne(id);
    }

    @PostMapping("/delete/2/{id}")
    public void deleteWarehouseTwo(@PathVariable Long id) {
        warehouseService.deleteInTwo(id);
    }
}
