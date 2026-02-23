package com.revature.revworkforce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String healthCheck() {
        return "RevWorkforce Application Running";
    }
    
    //Ensures app starts and endpoint works immediately.
}
