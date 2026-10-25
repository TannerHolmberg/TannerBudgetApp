package com.bank.tanner.budget.Service;

import com.bank.tanner.budget.Model.Gauge;
import com.bank.tanner.budget.Repository.GaugeRepository;

import java.util.List;

public class GaugeService {

    GaugeRepository gaugeRepository;

    public GaugeService(GaugeRepository gaugeRepository){
        this.gaugeRepository = gaugeRepository;
    }

    public Gauge createGauge(String name, Double budgetAmount){
        Gauge gauge = new Gauge(name, budgetAmount, null);
        if (!gaugeRepository.existsById(name)) {
            gaugeRepository.save(gauge);
        } else {
            gauge = null;
        }

        return(gauge);
    }

    public List<Gauge> getGauges(){
        List<Gauge> gauges = gaugeRepository.findAll();
        return gauges;
    }
}
