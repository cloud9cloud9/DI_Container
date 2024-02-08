package org.example.entity;

import org.example.annotation.Bean;

public class Car {
    @Bean
    public Car car(){
        return new Car();
    }

    @Override
    public String toString() {
        return "Car{}";
    }
}
