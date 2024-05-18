package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.api.DictionaryItem;
import org.codecrafterslab.unity.dict.boot.annotation.DictSerialize;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class SerializeHolder implements SerializeCondition {
    /**
     * 序列号键名配置
     */
    private final SerializeKey keys;
    /**
     * 序列号字段
     */
    private final List<SerializeScope> scopes;
    private SerializeCondition condition;

    public SerializeHolder(DictSerializeProperties properties, DictSerialize... dictSerialize) {
        this.keys = properties.keys;
        this.scopes = SerializeScope.findInnerSerializeScope(properties.getScopes());
        this.wrap(dictSerialize);
    }

    public SerializeHolder(SerializeHolder base, DictSerialize... dictSerialize) {
        this.scopes = base.scopes;
        this.keys = base.keys;
        this.wrap(dictSerialize);
    }

    @Override
    public List<SerializeScope> getScopes() {
        return scopes;
    }

    @Override
    public String getIdKey() {
        return keys.getIdKey();
    }

    @Override
    public String getCodeKey() {
        return keys.getCodeKey();
    }

    @Override
    public String getValueKey() {
        return keys.getValueKey();
    }

    @Override
    public String getLabelKey() {
        return keys.getLabelKey();
    }

    @Override
    public String getSortKey() {
        return keys.getSortKey();
    }

    @Override
    public String getDisabledKey() {
        return keys.getDisabledKey();
    }

    @Override
    public String getDescriptionKey() {
        return keys.getDescriptionKey();
    }

    protected boolean isWriteObject() {
        return getScopes().size() > 1;
    }

    public Object getObject(DictionaryItem<?> dictionaryItem) {
        if (isWriteObject()) return getMap(dictionaryItem);
        return getSingleValue(dictionaryItem);
    }

    private Object getSingleValue(DictionaryItem<?> dictItem) {
        Assert.isTrue(getScopes().size() == 1, "不能序列为单值");
        return getScopes().get(0).getValue(dictItem);
    }

    private Map<String, Object> getMap(DictionaryItem<?> dictItem) {
        HashMap<String, Object> data = new HashMap<>(getScopes().size());
        scopes.forEach(scope -> {
            String key = scope.getKey(this);
            Object value = scope.getValue(dictItem);
            data.put(key, value);
        });
        return data;
    }


    public SerializeHolder combine(DictSerialize... dictSerialize) {
        List<SerializeCondition> serializeConditions = dictSerializeConditions(dictSerialize);
        //        contextHolders.stream().reduce()
//        Arrays.stream(dictSerialize).filter(Objects::nonNull).forEach(dict -> {
//            List<SerializeScope> scopes = Arrays.asList(dict.value());
//            if (!ObjectUtils.isEmpty(scopes)) {
//                this.setScopes(scopes);
//            }
//            if (StringUtils.hasText(dict.id())) {
//                this.setId(dict.id());
//            }
//            if (StringUtils.hasText(dict.label())) {
//                this.setLabel(dict.label());
//            }
//            if (StringUtils.hasText(dict.description())) {
//                this.setDescription(dict.description());
//            }
//        });
//        if (serializeConditions.size() > 1) {
//            return this.combine(dictSerialize);
////            return new SerializeHolder(this, dictSerialize);
//        }
        return this;
    }

    private List<SerializeCondition> dictSerializeConditions(DictSerialize[] dictSerialize) {
        List<DictSerialize> collect =
                Arrays.stream(dictSerialize).filter(s -> !Objects.isNull(s)).collect(Collectors.toList());
        return Collections.emptyList();
    }


    private void wrap(DictSerialize... dictSerialize) {
//        SerializeCondition[] wrappedConditions = new SerializeCondition[conditions.length];
//
//        for (int i = 0; i < conditions.length; ++i) {
//            wrappedConditions[i] = new SerializeHolder(conditions[i]);
//        }
//
//        return wrappedConditions;
//        return null;
    }

}
