package com.m01project.taskmanager.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class HelloWorldService {

    public String sayHello() {
        return "Hello World!";
    }

    public String sayHelloTo(String name) {
        if(name != null && !name.isBlank()) {
            return "hello " + name.trim();
        }
        else {
            return "hello nobody!";
        }
    }
}