package com.study.teller.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.common.ApiResponse;
import com.study.teller.service.CashService;

@RestController
public class CashController {

    @Autowired
    private CashService cashService;

    @GetMapping("/cash")
    public ApiResponse cash() {
        return ApiResponse.ok(cashService.getCash());
    }

    @PostMapping("/cash/close")
    public ApiResponse close(@RequestBody Map<String, String> param) {
        long realAmt = Long.parseLong(param.get("realAmt"));
        return ApiResponse.ok(cashService.close(realAmt));
    }
}