package main.service;

import main.entity.Good;
import main.exception.GoodNotFoundException;
import main.repository.GoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GoodServiceImpl implements GoodService {

    @Autowired
    private GoodRepository goodRepository;

    @Override
    public List<Good> listGoods() {
        return (List<Good>) goodRepository.findAll();
    }

    @Override
    public Good findGood(Long id) {
        Optional<Good> goodsOptional = goodRepository.findById(id);
        if (goodsOptional.isPresent()) {
            return goodsOptional.get();
        } else {
            throw new GoodNotFoundException("Good not found");
        }
    }

    @Override
    public Good findGoodByName(String name) {
        Optional<Good> goodsOptional = goodRepository.findByName(name);
        if (goodsOptional.isPresent()) {
            return goodsOptional.get();
        } else {
            throw new GoodNotFoundException("Good not found");
        }
    }

    @Override
    public Good addGood(Good good) {
        return goodRepository.save(good);
    }

    @Override
    public void deleteGood(Long id) {
        goodRepository.deleteById(id);
    }
}
