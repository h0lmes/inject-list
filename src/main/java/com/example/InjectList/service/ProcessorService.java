package com.example.InjectList.service;

import com.example.InjectList.config.InjectComponentList;
import com.example.InjectList.processor.Processor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class ProcessorService {

    //@InjectComponentList(components = "{'firstPhaseProcessor', 'secondPhaseProcessor'}")
    @InjectComponentList(components = "#{'${application.processors}'.split(',')}")
    private List<Processor> processors;

    @PostConstruct
    public void postProcess() {
        processors.forEach(Processor::process);
    }
}
