package com.m01project.taskmanager.controller;

import com.m01project.taskmanager.service.HelloWorldService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    private final HelloWorldService helloWorldService;

    public HelloWorldController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    @GetMapping("/hello")
    public String sayHelloTo(
            @RequestParam(name = "username", defaultValue = "noBody") String name) {
        return helloWorldService.sayHelloTo(name);
    }
}
