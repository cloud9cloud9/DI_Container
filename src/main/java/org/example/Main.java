package org.example;

import org.example.context.ApplicationContext;
import org.example.context.ComponentContainer;
import org.example.entity.Car;
import org.example.entity.Human;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ApplicationContext();
        System.out.println(applicationContext.getBean("car"));

        ComponentContainer componentContainer = new ComponentContainer();
        System.out.println(componentContainer.getComponent(Human.class));
    }
}