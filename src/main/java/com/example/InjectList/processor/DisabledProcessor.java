package com.example.InjectList.processor;

import org.springframework.stereotype.Component;

@Component
public class DisabledProcessor implements Processor {

    @Override
    public void process() {
        System.out.println("you shouldn't see this normally");
    }
}
