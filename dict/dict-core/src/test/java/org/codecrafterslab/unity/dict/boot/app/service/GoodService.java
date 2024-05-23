package org.codecrafterslab.unity.dict.boot.app.service;

import org.codecrafterslab.unity.dict.boot.app.entity.Goods;

import java.util.List;

public interface GoodService {

    List<Goods> list();

    void save(Goods goods);

    Goods findById(Integer id);

}
