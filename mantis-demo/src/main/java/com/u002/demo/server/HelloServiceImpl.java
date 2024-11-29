package com.u002.demo.server;


import com.u002.demo.HelloService;
import com.u002.demo.Person;

public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        return "Hello! " + name;
    }

    public String hello(Person person) {
        return "Hello! " + person.getFirstName() + " " + person.getLastName();
    }
}
