package main.service;

import main.entity.Good;

import java.util.List;

public interface GoodService {
    List<Good> listGoods();
    Good findGood(Long id);
    Good findGoodByName(String name);
    Good addGood(Good good);
    void deleteGood(Long id);
}
