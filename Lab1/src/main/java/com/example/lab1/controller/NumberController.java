package com.example.lab1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NumberController {

    @GetMapping("/number")
    public int[] getNumber() {
        return new int[]{1, 2, 3, 4, 5};
    }
}
