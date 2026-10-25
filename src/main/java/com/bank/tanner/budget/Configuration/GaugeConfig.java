package com.bank.tanner.budget.Configuration;

import com.bank.tanner.budget.Controller.GaugeController;
import com.bank.tanner.budget.Repository.GaugeRepository;
import com.bank.tanner.budget.Service.GaugeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GaugeConfig {

    @Bean
    public GaugeController gaugeController(GaugeService gaugeService){
        return new GaugeController(gaugeService);
    }

    @Bean
    public GaugeService gaugeService(GaugeRepository gaugeRepository){
        return new GaugeService(gaugeRepository);
    }

}
