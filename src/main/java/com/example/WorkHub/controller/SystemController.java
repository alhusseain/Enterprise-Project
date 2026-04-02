package com.example.WorkHub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemController {

    @GetMapping("/health")
    public String healthCheck() {
        return "I'm alive :D";
    }

    // I just found this out while reading about response entity so i had to use it
    @GetMapping("/WhatAmI")
    public ResponseEntity<String> info() {
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }
}
