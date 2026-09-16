package com.study.teller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.common.ApiResponse;
import com.study.teller.service.AccountService;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/account/list")
    public ApiResponse list() {
        return ApiResponse.ok(accountService.getAccountList());
    }
}