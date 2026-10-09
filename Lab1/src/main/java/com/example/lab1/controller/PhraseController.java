package com.example.lab1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhraseController {

    @GetMapping("/phrase")
    public String getPhrase() {
        return "<Ассаламу алейкум>";
    }
}
