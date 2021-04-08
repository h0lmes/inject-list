package com.example.InjectList.processor;

import org.springframework.stereotype.Component;

@Component
public class SecondPhaseProcessor implements Processor {

    @Override
    public void process() {
        System.out.println("second phase");
    }
}
