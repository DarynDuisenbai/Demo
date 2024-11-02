package com.example.demo.controllers;

import com.example.demo.services.ConclusionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ConclusionController {
    private final ConclusionService conclusionService;
}
