package org.codecrafterslab.unity.dict.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author Wu Yujie
 */
@Slf4j
@RestController
@RequestMapping(value = "/dict", name = "字典")
public class DictController {

//    @Resource
//    private UserService userService;

    @Resource
    private GoodService goodService;

//    @GetMapping(value = "/converter/raw/value/integer", name = "原始方式 1")
//    public User rawParameter1(Integer sex) {
//        return userService.getSexByIntegerValue(sex);
//    }
//
//    @GetMapping(value = "/converter/raw/value/string", name = "原始方式 2")
//    public User rawParameter2(String sex) {
//        return userService.getSexByStringValue(sex);
//    }
//
//    @GetMapping(value = "/converter/raw/code", name = "原始方式 3")
//    public User rawParameter3(String sex) {
//        return userService.getSexByCode(sex);
//    }
//
//    @GetMapping(value = "/converter/enum", name = "枚举字典使用")
//    public User enumParameter4(Sex sex) {
//        return userService.getSex(sex);
//    }

    @GetMapping(value = "/converter/enum/func", name = "原始方式 1")
    public List<ProductService> rawParameter1(@RequestParam("services") ProductService[] services,
                                              @RequestParam("services") List<ProductService> services1) {
        List<ProductService> list = Arrays.asList(services);
        log.debug("{}", list);
        return list;
    }

    @GetMapping(value = "/func/test", name = "功能点枚举反序列化测试")
    public List<Goods> getGoods() {
        return goodService.list();
    }

    @PostMapping(value = "/func/test", name = "功能点枚举反序列化测试")
    public void saveGoods(@RequestBody Goods goods) {
        log.info("{}", goods);
        goodService.save(goods);
    }

    @GetMapping(value = "/func/test/{id}", name = "功能点枚举反序列化测试")
    public Goods getGoodsById(@PathVariable Integer id) {
        return goodService.findById(id);
    }
}
