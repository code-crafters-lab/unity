package org.codecrafterslab.unity.dict.boot.provider;

import lombok.Getter;
import org.codecrafterslab.unity.dict.api.EnumDictItem;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class EnumDictProviderBuilder {
    private final Set<Class<? super EnumDictItem<?>>> classes;
    private final List<TypeFilter> excludeFilters;
    private boolean globalScan = false;
    private Set<String> scanPackages;

    public EnumDictProviderBuilder() {
        this.classes = new HashSet<>();
        this.excludeFilters = new ArrayList<>();
        this.scanPackages = new HashSet<>();
    }

    public EnumDictProviderBuilder globalScan(boolean globalScan) {
        this.globalScan = globalScan;
        return this;
    }

    public EnumDictProviderBuilder scan(List<String> packageNames) {
        return packages(list -> list.addAll(packageNames));
    }

    public EnumDictProviderBuilder scan(String packageName, String... otherPackageNames) {
        List<String> list = Stream.concat(Stream.of(packageName), Stream.of(otherPackageNames)).collect(Collectors.toList());
        return scan(list);
    }

    public EnumDictProviderBuilder packages(Consumer<List<String>> packageNames) {
        List<String> list = new LinkedList<>(this.scanPackages);
        packageNames.accept(list);
        this.scanPackages = new HashSet<>(list);
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

    public EnumDictProviderBuilder add(Class<EnumDictItem<?>> clazz) {
        classes.add(clazz);
        return this;
    }

    public EnumDictProviderBuilder add(Collection<Class<EnumDictItem<?>>> list) {
        classes.addAll(list);
        return this;
    }

    private void reset() {
        classes.clear();
    }

    public EnumDictProvider build() {
        this.reset();
        this.processScanClasses();
        return new EnumDictProvider(classes);
    }

    private void processScanClasses() {
        if (globalScan && scanPackages.isEmpty()) {
            Class<?> mainApplicationClass = deduceMainApplicationClass();
            if (null != mainApplicationClass) {
                String packageName = ClassUtils.getPackageName(mainApplicationClass);
                scanPackages.add(packageName);
            }
        }

        for (String packageName : scanPackages) {
            Collection<Class<? super EnumDictItem<?>>> result = packageScan(packageName,
                    excludeFilters.toArray(new TypeFilter[]{}));
            this.classes.addAll(result);
        }
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

    @SuppressWarnings("unchecked")
    private Collection<Class<? super EnumDictItem<?>>> packageScan(String enumDictPackage,
                                                                   TypeFilter... excludeFilters) {
        if (!StringUtils.hasText(enumDictPackage)) return Collections.emptyList();

        /* 查找 package 下枚举字典实现类 */
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(EnumDictItem.class));
        Arrays.stream(excludeFilters).forEach(provider::addExcludeFilter);
        Set<BeanDefinition> components = provider.findCandidateComponents(enumDictPackage);

        List<Class<? super EnumDictItem<?>>> list = components.stream()
                .map(BeanDefinition::getBeanClassName)
                .filter(Objects::nonNull)
                .map(clazz -> {
                    try {
                        return (Class<EnumDictItem<?>>) ClassUtils.forName(clazz, null);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
        return Collections.unmodifiableCollection(list);
    }
}
