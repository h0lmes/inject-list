package com.example.InjectList.processor;

import org.springframework.stereotype.Component;

@Component
public class FirstPhaseProcessor implements Processor {

    @Override
    public void process() {
        System.out.println("first phase");
    }
}
