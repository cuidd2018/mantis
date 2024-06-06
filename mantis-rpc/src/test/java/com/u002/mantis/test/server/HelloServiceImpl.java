package com.u002.mantis.test.server;

import com.u002.mantis.test.client.HelloService;
import com.u002.mantis.test.client.Person;

public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        return "Hello! " + name;
    }

    public String hello(Person person) {
        return "Hello! " + person.getFirstName() + " " + person.getLastName();
    }
}
