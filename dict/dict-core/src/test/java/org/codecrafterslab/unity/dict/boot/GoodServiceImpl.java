package org.codecrafterslab.unity.dict.boot;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodServiceImpl implements GoodService {
    private final Map<Integer, Goods> maps = new HashMap<>();

    @Override
    public List<Goods> list() {
        return new ArrayList<>(maps.values());
    }

    @Override
    public void save(Goods goods) {
        goods.setId(maps.size() + 1);
        this.maps.put(goods.getId(), goods);
    }

    @Override
    public Goods findById(Integer id) {
        return maps.get(id);
    }
}
