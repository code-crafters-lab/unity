package org.codecrafterslab.unity.dict.boot.json.jackson.ser;

import lombok.Getter;
import org.codecrafterslab.unity.dict.api.FuncEnumDictItem;
import org.codecrafterslab.unity.dict.boot.combine.Scope;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum SerializeScope implements FuncEnumDictItem {
    ID(Scope.ID),
    CODE(Scope.CODE),
    VALUE(Scope.VALUE),
    LABEL(Scope.LABEL),
    SORT(Scope.SORT),
    DISABLED(Scope.DISABLED),
    DESCRIPTION(Scope.DESCRIPTION),

    CODE_LABEL(Scope.CODE, Scope.LABEL),
    VALUE_LABEL(Scope.VALUE, Scope.LABEL),
    CODE_VALUE_LABEL(Scope.CODE, Scope.VALUE, Scope.LABEL),

    ALL(Scope.values());

    private final List<Scope> scopes;
    private final Integer value;
    private Scope scope = null;
    private boolean inner = false;

    SerializeScope(Set<Scope> scopes) {
        this.scopes = new ArrayList<>(scopes);
        this.value = scopes.stream().reduce(0, (a, b) -> a | b.getValue(), (a, b) -> a | b);
        if (scopes.size() == 1) {
            this.scope = scopes.iterator().next();
            this.inner = scopes.size() == 1;
        }
    }

    SerializeScope(Scope... scopes) {
        this(new HashSet<>(Arrays.asList(scopes)));
    }

    public static List<Scope> findScopes(Collection<SerializeScope> all) {
        return all.stream().flatMap((Function<SerializeScope, Stream<Scope>>)
                        serializeScope -> serializeScope.getScopes().stream())
                .distinct().collect(Collectors.toList());
    }

    public static List<Scope> findScopes(SerializeScope[] all) {
        return findScopes(Arrays.asList(all));
    }

    @Override
    public BigInteger getValue() {
        return BigInteger.valueOf(value);
    }


    @Override
    public String getName() {
        return value.toString();
    }

}
