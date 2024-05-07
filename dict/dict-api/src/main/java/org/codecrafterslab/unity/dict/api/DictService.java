package org.codecrafterslab.unity.dict.api;

import java.io.Serializable;
import java.util.List;

/**
 * @since 1.0.0
 */
public interface DictService {

    /**
     * 新增字典
     *
     * @param dict {@link Dictionary}
     */
    void save(Dictionary<?> dict);

    /**
     * 更新字典
     *
     * @param id   唯一标识
     * @param dict Dictionary<V>
     */
    <ID extends Serializable> void update(ID id, Dictionary<?> dict);

    /**
     * 新增或更新字典
     *
     * @param dict Dictionary<V>
     */
    void saveOrUpdate(Dictionary<?> dict);

    /**
     * 删除字典
     *
     * @param dict  Dictionary<V>
     * @param logic 是否逻辑删除
     */
    boolean delete(Dictionary<?> dict, boolean logic);

    /**
     * 删除字典
     *
     * @param id 唯一标识
     * @return 是否成功，成功为 {@code true}，否则为 {@code false}
     */
    <ID extends Serializable> boolean deleteById(ID id, boolean logic);

    /**
     * 查询字典列表
     *
     * @param query 查询条件
     * @return 字典列表
     */
    List<Dictionary<?>> select(Dictionary.Query query);

    /**
     * 分页查询字典列表
     *
     * @param query 查询条件
     * @return 字典列表
     */
    List<Dictionary<?>> selectPage(Dictionary.Query query, Object pab);

    /**
     * 查询字典详情
     *
     * @param id 字典 ID
     * @return 字典详细信息
     */
    <ID extends Serializable> Dictionary<?> getDictDetail(ID id);

    /**
     * 获取字典项
     *
     * @param code 字典编码
     * @return 字典项列表
     */
    List<DictionaryItem<?>> getDictItemByCode(String code);

}
