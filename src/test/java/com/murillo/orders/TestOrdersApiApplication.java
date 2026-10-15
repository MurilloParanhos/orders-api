package com.murillo.orders;

import org.springframework.boot.SpringApplication;

public class TestOrdersApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(OrdersApiApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
