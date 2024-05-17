package org.codecrafterslab.unity.dict.boot;

import java.util.List;

public interface GoodService {

    List<Goods> list();

    void save(Goods goods);

    Goods findById(Integer id);

}
