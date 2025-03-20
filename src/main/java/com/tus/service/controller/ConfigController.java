package com.tus.service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @GetMapping
    public String showConfig() {
        return "DB URL: " + dbUrl + ", JWT Secret: " + jwtSecret;
    }
}
