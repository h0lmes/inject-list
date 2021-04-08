package com.example.InjectList.processor;

import org.springframework.stereotype.Component;

@Component
public class PreProcessor implements Processor {

    @Override
    public void process() {
        System.out.println("pre process");
    }
}
