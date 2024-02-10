package org.example.context;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.example.annotation.Bean;
import org.example.util.FilePath;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationContext {
    private static final Map<String, Object> beans = new HashMap<>();
    private final String filePath;

    public ApplicationContext() {
        this.filePath = FilePath.filePathByContextApp;
        register(getInstance());
    }

    public List<Object> getInstance() {
        List<Object> objects = null;
        List<String> clazz = new ArrayList<>();
        try (ScanResult scanResult = new ClassGraph()
                .acceptPackages(filePath)
                .scan()) {
            ClassInfoList allClasses = scanResult.getAllClasses();
            for (ClassInfo classInfo : allClasses) {
                clazz.add(classInfo.getName());
            }
            if (!clazz.isEmpty()) {
                 objects = clazz.stream()
                        .map(this::createNewInstance)
                        .collect(Collectors.toList());
            }
        }
        return objects;
    }
    private Object createNewInstance(String className){
        try {
            Class<?> classes = Class.forName(className);
            return classes.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    public void register(List<Object> listObj) {
        for (Object instance : listObj) {
            Method[] methods = instance.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Bean.class)) {
                    try {
                        Object bean = method.invoke(instance);
                        beans.put(method.getName(), bean);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Object getBean(String name) {
        return beans.get(name);
    }
    public Object getBean(Class<?> clazz) {
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            if (clazz.isInstance(entry.getValue())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
