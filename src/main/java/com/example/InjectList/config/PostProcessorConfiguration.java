package com.example.InjectList.config;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostProcessorConfiguration {

    @Bean
    BeanPostProcessor injectComponentListBPP() {
        return new InjectComponentListBPP();
    }
}
