package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import lombok.extern.slf4j.Slf4j;
import org.codecrafterslab.unity.dict.boot.combine.Scope;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

@Slf4j
class SerializeScopeTest {
    
    @Test
    void findScopes() {
        List<Scope> scopes = SerializeScope.findScopes(Arrays.asList(SerializeScope.values()));
        Assertions.assertEquals(scopes.size(), Scope.values().length);
        for (Scope scope : Scope.values()) {
            Assertions.assertTrue(scopes.contains(scope));
        }
    }

    @Test
    void getValue() {
        for (SerializeScope serializeScope : SerializeScope.values()) {
            Scope s = serializeScope.getScope();
            if (serializeScope.isInner()) {
                Assertions.assertNotNull(s);
                Assertions.assertEquals(serializeScope.getValue().intValue(), s.getValue());
            } else {
                Assertions.assertNull(s);
            }
        }
    }
}