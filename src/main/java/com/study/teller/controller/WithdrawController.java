package com.study.teller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.teller.common.ApiResponse;
import com.study.teller.service.WithdrawService;
import com.study.teller.vo.WithdrawReqVo;

@RestController
public class WithdrawController {

    @Autowired
    private WithdrawService withdrawService;

    @PostMapping("/withdraw")
    public ApiResponse withdraw(@RequestBody WithdrawReqVo vo) throws Exception {
        return ApiResponse.ok(withdrawService.withdraw(vo));
    }
}