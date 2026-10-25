package com.bank.tanner.budget.Controller;

import com.bank.tanner.budget.Model.Gauge;
import com.bank.tanner.budget.Service.GaugeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gauge")
public class GaugeController {

    GaugeService gaugeService;

    public GaugeController(GaugeService gaugeService){
        this.gaugeService = gaugeService;
    }

    @PostMapping("/create-gauge")
    public Gauge createGauge(
            @RequestParam String gaugeName,
            @RequestParam Double budgetAmount
    ){
        return gaugeService.createGauge(gaugeName, budgetAmount);
    }

    @GetMapping("/get-gauges")
    public List<Gauge> getGauges(){
        return gaugeService.getGauges();
    }

}
