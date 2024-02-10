package org.example.context;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.example.annotation.Component;
import org.example.util.FilePath;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ComponentContainer {
    private final Map<String, Object> components = new HashMap<>();

    public ComponentContainer() {
        scanAndInstantiate();
    }

    public void scanAndInstantiate() {
        ClassGraph classGraph = new ClassGraph().enableAllInfo().acceptPackages(FilePath.filePathByContextApp);
        try (ScanResult scanResult = classGraph.scan()) {
            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(Component.class.getName())) {
                String className = classInfo.getName();
                Component componentAnnotation = classInfo.loadClass().getAnnotation(Component.class);
                String componentName = componentAnnotation.name();
                try {
                    Object instance = Class.forName(className).getDeclaredConstructor().newInstance();
                    components.put(componentName, instance);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addComponent(String componentName, Object instance) {
        components.put(componentName, instance);
    }

    public Object getComponent(String componentName) {
        return components.get(componentName);
    }
    public Object getComponent(Class<?> clazz) {
        for (Map.Entry<String, Object> entry : components.entrySet()) {
            if (clazz.isInstance(entry.getValue())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
