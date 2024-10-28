package com.example.demo.controllers;

import com.example.demo.services.ConclusionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ConclusionController {
    private final ConclusionService conclusionService;

    @Autowired
    public ConclusionController(ConclusionService conclusionService) {
        this.conclusionService = conclusionService;
    }
}
