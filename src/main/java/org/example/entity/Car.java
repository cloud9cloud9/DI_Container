package org.example.entity;

import org.example.annotation.Bean;

public class Car {

    private String model;

    public Car(String model) {
        this.model = model;
    }

    public Car() {

    }

    @Bean
    public Car car() {
        Car car = new Car("merc");
        return car;
    }

    @Override
    public String toString() {
        return "Car{" +
                "model='" + model + '\'' +
                '}';
    }
}
