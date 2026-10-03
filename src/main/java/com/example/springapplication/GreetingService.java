package com.example.springapplication;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    public Greeting greet(String name) {
        return new Greeting("Hello, " + name + "!");
    }
}
