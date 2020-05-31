package com.training.demo;

import com.training.demo.controllers.util.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ContextConfiguration;

@SpringBootApplication
@ContextConfiguration(classes = { AppConfig.class })
public class MangSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(MangSysApplication.class, args);
    }

}
