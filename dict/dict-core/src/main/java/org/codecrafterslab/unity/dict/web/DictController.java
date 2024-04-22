package org.codecrafterslab.unity.dict.web;

import org.codecrafterslab.unity.dict.api.DictService;
import org.codecrafterslab.unity.dict.api.Dictionary;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/dict")
public class DictController {

    private final DictService dictService;

    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @PostMapping
    public void save(@RequestBody @Validated Dictionary<?> dict) {
        dictService.save(dict);
    }

    @DeleteMapping(value = "/{id}", name = "删除字典")
    public boolean deleteById(@PathVariable("id") String dictId) {
        return dictService.deleteById(dictId, true);
    }

    @PutMapping(path = "/{id}", name = "修改字典")
    public void update(@PathVariable("id") String dictId, @RequestBody @Validated Dictionary<?> dictBO) {
        dictService.update(dictId, dictBO);
    }

    @GetMapping(name = "查询字典列表")
    public List<Dictionary<?>> list(Dictionary.Query query) {
        return this.dictService.select(query);
    }

    @GetMapping(value = "/{id}", name = "字典详情")
    public Dictionary<?> get(@PathVariable("id") String dictId) {
        return this.dictService.getDictDetail(dictId);
    }

    @GetMapping(path = "/item", name = "字典项列表")
    public List<DictionaryItem<?>> getDictItemById(@RequestParam(value = "code") String dictCode) {
        return this.dictService.getDictItemByCode(dictCode);
    }

}
