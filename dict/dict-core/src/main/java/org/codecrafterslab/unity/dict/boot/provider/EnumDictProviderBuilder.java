package org.codecrafterslab.unity.dict.boot.provider;

import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Stream;

public class EnumDictProviderBuilder {
    private final Set<Class<? extends EnumDictItem<?>>> classes;
    private final List<TypeFilter> excludeFilters;
    private boolean globalScan = false;
    private String scanPackage;

    public EnumDictProviderBuilder() {
        this.classes = new HashSet<>();
        this.excludeFilters = new ArrayList<>();
    }

    public EnumDictProviderBuilder globalScan(boolean globalScan) {
        this.globalScan = globalScan;
        return this;
    }

    public EnumDictProviderBuilder scan(String packageName) {
        this.scanPackage = packageName;
        return this;
    }

    public EnumDictProviderBuilder exclude(TypeFilter filter, TypeFilter... filters) {
        Stream.concat(Stream.of(filter), Arrays.stream(filters)).forEach(excludeFilters::add);
        return this;
    }

    public EnumDictProviderBuilder exclude(Class<?> clazz,
                                           Class<?>... classes) {
        TypeFilter[] typeFilters = Arrays.stream(classes)
                .filter(EnumDictItem.class::isAssignableFrom)
                .map(AssignableTypeFilter::new)
                .toArray(TypeFilter[]::new);
        return this.exclude(new AssignableTypeFilter(clazz), typeFilters);
    }

    public EnumDictProviderBuilder add(Class<? extends EnumDictItem<?>> clazz) {
        classes.add(clazz);
        return this;
    }

    public EnumDictProviderBuilder add(Collection<Class<? extends EnumDictItem<?>>> list) {
        classes.addAll(list);
        return this;
    }

    public EnumDictProviderBuilder reset() {
        classes.clear();
        return this;
    }

    public EnumDictProvider build() {
        this.reset();
        this.processScanClasses();
        return new EnumDictProvider(classes);
    }

    private void processScanClasses() {
        String packageName = scanPackage;
        if (globalScan && !StringUtils.hasText(scanPackage)) {
            Class<?> mainApplicationClass = deduceMainApplicationClass();
            if (null != mainApplicationClass) {
                packageName = ClassUtils.getPackageName(mainApplicationClass);
            }
        }
        Collection<Class<? extends EnumDictItem<?>>> classes1 = packageScan(packageName,
                excludeFilters.toArray(new TypeFilter[]{}));
        this.classes.addAll(classes1);
    }

    /**
     * 获取启动程序入楼类
     *
     * @return Class<?>
     */
    private Class<?> deduceMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (ClassNotFoundException ignored) {

        }
        return null;
    }

    private Collection<Class<? extends EnumDictItem<?>>> packageScan(String enumDictPackage,
                                                                     TypeFilter... excludeFilters) {
        if (!StringUtils.hasText(enumDictPackage)) return Collections.emptyList();

        /* 查找 package 下枚举字典实现类 */
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(EnumDictItem.class));
        Arrays.stream(excludeFilters).forEach(provider::addExcludeFilter);
        Set<BeanDefinition> components = provider.findCandidateComponents(enumDictPackage);

        List<Class<? extends EnumDictItem<?>>> list = new LinkedList<>();
        for (BeanDefinition component : components) {
            try {
                Class<?> cls = ClassUtils.forName(Objects.requireNonNull(component.getBeanClassName()), null);
                @SuppressWarnings("unchecked")
                Class<EnumDictItem<?>> baseEnumClass = (Class<EnumDictItem<?>>) cls;
                list.add(baseEnumClass);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return Collections.unmodifiableCollection(list);
    }
}
