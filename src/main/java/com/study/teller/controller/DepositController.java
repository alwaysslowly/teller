package com.study.teller.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.common.ApiResponse;
import com.study.teller.service.DepositService;
import com.study.teller.vo.DepositReqVo;

@RestController
public class DepositController {

    @PostMapping("/deposit")
    public ApiResponse deposit(@RequestBody DepositReqVo vo) throws Exception {

        DepositService service = new DepositService();
        return ApiResponse.ok(service.deposit(vo));
    }
}